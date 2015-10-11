Gradle docker plugin
======================
[ ![Download](https://api.bintray.com/packages/baptistemesta/maven/docker-build/images/download.svg) ](https://bintray.com/baptistemesta/maven/docker-build/_latestVersion)

Simple plugin to package docker image

How to use
--------
example:
```
buildscript {
    repositories {
        mavenLocal()
        jcenter()
    }
    dependencies {
        classpath 'com.bmesta.docker:docker-build:0.9.1'
    }
}
apply plugin: 'distribution'
apply plugin: 'docker-build'

group = "com.myproject"
version = "1.0.0-SNAPSHOT"

docker {
    serverUrl = System.getProperty("docker.url")
    saveImageTo = new File(buildDir, "docker-image.tar")
}
distributions {
    main {
        contents {
            from { docker.saveImageTo }
        }
    }
}
tasks.distZip.dependsOn(tasks.buildDockerImage)
```
