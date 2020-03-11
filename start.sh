#!/bin/bash
rm -rf /usr/local/project/new_ssl/src/main/java/com
rm -rf /var/opt/ssl/data
rm -f /usr/local/project/new_ssl/pom.xml
/bin/cp -fR /usr/local/docker/dummy_api/volume/com /usr/local/project/new_ssl/src/main/java/com
/bin/cp -fR /usr/local/docker/dummy_api/volume/data /var/opt/ssl/data
/bin/cp -fR /usr/local/docker/dummy_api/volume/pom/pom.xml /usr/local/project/new_ssl/pom.xml

mvn clean
mvn -Dspring-boot.run.jvmArguments=-Dspring.config.location=/var/opt/ssl/data/application-docker.yml spring-boot:run > /dev/null 2>&1 &
tail -F /tmp/myLog.log

