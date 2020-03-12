#!/bin/bash
if [ "$1" = 'first' ]; then
  echo 'start...'
else
  echo 'restart...'
  sleep 5
fi

rm -rf /usr/local/project/new_ssl/src/main/java/com
mkdir -p /usr/local/project/new_ssl/src/main/java/com
/bin/cp -fR /usr/local/docker/dummy_api/volume/com/* /usr/local/project/new_ssl/src/main/java/com

rm -rf /var/opt/ssl/data
mkdir -p /var/opt/ssl/data
/bin/cp -fR /usr/local/docker/dummy_api/volume/data/* /var/opt/ssl/data

rm -f /usr/local/project/new_ssl/pom.xml
/bin/cp -f /usr/local/docker/dummy_api/volume/pom/pom.xml /usr/local/project/new_ssl/pom.xml

rm -f /tmp/myLog.log
mvn clean
mvn -Dspring-boot.run.jvmArguments=-Dspring.config.location=/var/opt/ssl/data/application-docker.yml spring-boot:run >/dev/null 2>&1 &

if [ "$1" = 'first' ]; then
  tail -F /tmp/myLog.log
fi
