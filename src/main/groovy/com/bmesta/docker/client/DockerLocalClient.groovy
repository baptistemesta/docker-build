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

import org.gradle.api.GradleException
/**
 * @author Baptiste Mesta
 */
class DockerLocalClient implements DockerClient{

    String binary

    String buildImage(File buildDir, String tag) {
        return executeAndWait("${binary} build -t ${tag} ${buildDir}")
    }

    @Override
    def saveImage(String tag, File toFile) {
        executeAndWait("${binary} save -o ${toFile.getAbsolutePath()} ${tag}")
    }

    private static String executeAndWait(String cmdLine) {
        def process = cmdLine.execute()
        process.waitFor()
        if (process.exitValue()) {
            throw new GradleException("Docker execution failed\nCommand line [${cmdLine}] returned:\n${process.err.text}")
        }
        return process.in.text
    }
}
