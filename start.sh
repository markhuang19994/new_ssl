#!/bin/bash
/bin/cp -fR /usr/local/docker/volume/com /usr/local/project/new_ssl/src/main/java/com
/bin/cp -fR /usr/local/docker/volume/data /var/opt/ssl/data
/bin/cp -fR /usr/local/docker/volume/pom /usr/local/project/new_ssl/pom_dir

mvn clean
mvn -Dspring-boot.run.jvmArguments=-Dspring.config.location=/var/opt/ssl/data/application-docker.yml spring-boot:run
