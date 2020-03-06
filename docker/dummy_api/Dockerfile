FROM openjdk:8 as ssl_jdk

USER root

COPY ./resources/apache-maven-3.6.3-bin.tar.gz /tmp/apache-maven-3.6.3-bin.tar.gz

RUN \
    mkdir -p /usr/local/project  && \
    cd /usr/local/project && \
    git clone -b new_ssl --single-branch --progress --verbose https://github.com/markhuang19994/ssl.git && \
    cd ssl && git checkout -f new_ssl && \
    rm -rf src/main/resources/application.yml && \
    \
    tar xf /tmp/apache-maven-*.tar.gz -C /opt && \
    rm -rf /temp/apache-maven* && \
    ln -s /opt/apache-maven-3.6.3 /opt/maven && \
    \
    apt-get update && \
    apt-get -y install vim && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

ENV M2_HOME=/opt/maven
ENV MAVEN_HOME=/opt/maven
ENV PATH=${M2_HOME}/bin:${PATH}

WORKDIR /usr/local/project/ssl
CMD ["mvn", "-Dspring-boot.run.jvmArguments=-Dspring.config.location=/var/opt/ssl/data/application-docker.yml", "clean", "spring-boot:run"]
