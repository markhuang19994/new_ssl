FROM openjdk:8 as ssl_jdk

USER root

ARG maven_url='https://drive.google.com/uc?export=download&id=1NDxF0iLDCKtWzqTYJE34tWZeAzzk-Zwn'
ARG rep_a_url='https://drive.google.com/uc?export=download&id=1ycRzZ7oReWLUXTkUIfpf3jya4IhAgMSV'
ARG rep_b_url='https://drive.google.com/uc?export=download&id=1dVKj-YLuYrkVlxe9hyIGfnhsoAIGZcnv'

RUN \
    mkdir -p /tmp/rc && \
    wget --no-check-certificate "${maven_url}" -O /tmp/rc/maven.tar.gz && \
    wget --no-check-certificate "${rep_a_url}" -O /tmp/rc/rep.tar.gz.a && \
    wget --no-check-certificate "${rep_b_url}" -O /tmp/rc/rep.tar.gz.b && \
    \
    tar xf /tmp/rc/maven.tar.gz -C /opt && \
    ln -s /opt/maven /opt/maven && \
    \
    mkdir -p /root/.m2 && \
    cat /tmp/rc/rep.tar.gz.* | tar -zxf - -C /root/.m2 && \
    \
    apt-get update && \
    apt-get -y install vim && \
    apt-get clean && rm -rf /var/lib/apt/lists/* && \
    rm -rf /tmp/rc

#busted github cache
ADD https://api.github.com/repos/markhuang19994/new_ssl/git/refs/heads/master version.json
RUN \
    mkdir -p /usr/local/project  && \
    cd /usr/local/project && \
    git clone --progress --verbose https://github.com/markhuang19994/new_ssl.git && \
    cd new_ssl && \
    mkdir pom_dir && \
    chmod +x ./start.sh && \
    rm -rf ./src/main/resources/application.yml

ENV M2_HOME=/opt/maven
ENV MAVEN_HOME=/opt/maven
ENV PATH=${M2_HOME}/bin:${PATH}

WORKDIR /usr/local/project/new_ssl
CMD ['./start.sh']
#CMD ["mvn", "-Dspring-boot.run.jvmArguments=-Dspring.config.location=/var/opt/ssl/data/application-docker.yml", "clean", "spring-boot:run"]
