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

package com.bmesta.docker

import com.bmesta.docker.client.DockerClient
import com.bmesta.docker.client.DockerLocalClient
import com.bmesta.docker.client.DockerRemoteClient
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * @author Baptiste Mesta
 */
class DockerTask extends DefaultTask {

    DockerPluginExtension dockerPluginExtension

    @TaskAction
    def buildDockerImage() {
        DockerClient dockerClient
        if (dockerPluginExtension.serverUrl != null) {
            dockerClient = new DockerRemoteClient(dockerPluginExtension.serverUrl, dockerPluginExtension.username, dockerPluginExtension.password, dockerPluginExtension.email);
        } else {
            dockerClient = new DockerLocalClient(binary: dockerPluginExtension.dockerBinary);
        }
        dockerClient.buildImage(project.ext.dockerBuildDir, dockerPluginExtension.tag)
        if (dockerPluginExtension.saveImageTo != null) {
            dockerClient.saveImage(dockerPluginExtension.tag, dockerPluginExtension.saveImageTo)
        }
    }
}
