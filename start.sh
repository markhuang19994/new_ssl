#!/bin/bash
/bin/cp -fR /usr/local/docker/dummy_api/volume/com /usr/local/project/new_ssl/src/main/java/com
/bin/cp -fR /usr/local/docker/dummy_api/volume/data /var/opt/ssl/data
/bin/cp -fR /usr/local/docker/dummy_api/volume/pom/pom.xml /usr/local/project/new_ssl/pom.xml

mvn clean
mvn -Dspring-boot.run.jvmArguments=-Dspring.config.location=/var/opt/ssl/data/application-docker.yml spring-boot:run
