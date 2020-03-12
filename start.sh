#!/bin/bash
if [ "$1" = 'first' ]; then
  echo 'start...'
  rm -f /tmp/myLog.log
  cd /usr/local/docker/dummy_api || exit
  git fetch --progress origin master
  git clean -f
  git checkout -f origin/master
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

cd /usr/local/project/new_ssl || exit
mvn clean
spring_jvm_args='-Dspring-boot.run.jvmArguments=-Dspring.config.location=/var/opt/ssl/data/application-docker.yml'
if [ "$1" = 'first' ]; then
  mvn "$spring_jvm_args" spring-boot:run >/dev/null 2>&1 &
  tail -F /tmp/myLog.log
else
  mvn "$spring_jvm_args" spring-boot:run
fi
