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

import org.gradle.api.Plugin
import org.gradle.api.Project
/**
 * @author Baptiste Mesta
 */
class DockerPlugin implements Plugin<Project> {

    static final String PLUGIN_NAME = "docker-build"
    static final String PLUGIN_TASK_GROUP = PLUGIN_NAME

    @Override
    void apply(Project project) {

        def properties = project.extensions.create("docker", DockerPluginExtension)
        properties.with {
            dockerBinary = 'docker'
            dockerfileLocation = 'src/main/docker'
            tag = "${project.name}"
            saveImageTo = new File(project.buildDir, "${tag}.tar")
        }
        project.ext{
            dockerBuildDir = new File(project.getBuildDir(),"docker");
        }
        project.task("buildDockerImage", type: DockerTask){
            doFirst {
                project.dockerBuildDir.mkdirs()
                project.copy {
                    from properties.dockerfileLocation
                    into project.dockerBuildDir
                }
            }

            group = PLUGIN_TASK_GROUP
            description = 'build a docker image from a docker file'
            dockerPluginExtension = properties
        }
    }
}
