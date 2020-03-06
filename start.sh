#!/bin/bash
[[ ! -f ./pom_dir ]] && cp pom.xml ./pom_dir
mvn clean
mvn -Dspring-boot.run.jvmArguments=-Dspring.config.location=/var/opt/ssl/data/application-docker.yml spring-boot:run
