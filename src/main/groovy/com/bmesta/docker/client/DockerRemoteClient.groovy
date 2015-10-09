/*
 * Copyright (C) 2015 Baptiste Mesta
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 *
 */

package com.bmesta.docker.client
import com.github.dockerjava.core.DockerClientBuilder
import com.github.dockerjava.core.DockerClientConfig
import com.github.dockerjava.core.command.BuildImageResultCallback
import org.apache.commons.lang.StringUtils
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
/**
 * @author Baptiste Mesta
 */
class DockerRemoteClient implements DockerClient {
    private static Logger log = Logging.getLogger(DockerRemoteClient.class);

    private com.github.dockerjava.api.DockerClient dockerClient;


    public DockerRemoteClient(String url, String user, String password, String email) {
        final DockerClientConfig.DockerClientConfigBuilder configBuilder = DockerClientConfig.createDefaultConfigBuilder();
        if (StringUtils.isEmpty(url)) {
            log.info("Connecting to localhost");
        } else {
            log.info("Connecting to {}", url);
            configBuilder.withUri(url);
        }
        if (StringUtils.isNotEmpty(user)) {
            configBuilder.withUsername(user).withPassword(password).withEmail(email);
        }
        dockerClient = DockerClientBuilder.getInstance(configBuilder).build();
    }

    @Override
    def String buildImage(File buildDir, String tag) {
        final BuildImageResultCallback resultCallback = new BuildImageResultCallback();
        dockerClient.buildImageCmd(buildDir).withTag(tag).exec(resultCallback);
        return resultCallback.awaitImageId();
    }

    @Override
    def saveImage(String tag, File toFile) {
        dockerClient.saveImageCmd(tag).exec().withStream { inputStream ->
            toFile.withDataOutputStream {outputStream->
                byte[] buffer = new byte[10 * 1024];
                for (int length; (length = inputStream.read(buffer)) != -1;) {
                    outputStream.write(buffer, 0, length);
                    outputStream.flush();
                }
            }
        }
    }
}
