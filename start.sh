#!/bin/bash
[[ ! -f ./pom_dir/pom.xml ]] && cp pom.xml ./pom_dir
rm -rf pom.xml && cp ./pom_dir/pom.xml .
mvn clean
mvn -Dspring-boot.run.jvmArguments=-Dspring.config.location=/var/opt/ssl/data/application-docker.yml spring-boot:run
