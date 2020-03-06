[[ ! -f ./pom_dir ]] && cp pom.xml ./pom_dir
mvn -f ./pom_dir clean
mvn -f ./pom_dir -Dspring-boot.run.jvmArguments=-Dspring.config.location=/var/opt/ssl/data/application-docker.yml spring-boot:run
