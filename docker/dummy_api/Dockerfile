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
    sleep 5 && \
    \
    tar xf /tmp/rc/maven.tar.gz -C /opt && \
    ln -s /opt/maven /opt/maven && \
    \
    mkdir -p /root/.m2 && \
    cat /tmp/rc/rep.tar.gz.* | tar -zxf - -C /root/.m2 && \
    rm -rf /tmp/rc

RUN \
    mkdir -p /usr/local/docker  && \
    cd /usr/local/docker && \
    git clone --progress --verbose http://markhuang:markhuang%40iisi1@finsrv01.iead.local/git/CITI_DUMMY_API.git && \
    mv CITI_DUMMY_API dummy_api

#busted github cache
ADD https://api.github.com/repos/markhuang19994/new_ssl/git/refs/heads/sit version.json
RUN \
    mkdir -p /usr/local/project  && \
    cd /usr/local/project && \
    git clone --progress --verbose https://github.com/markhuang19994/new_ssl.git && \
    cd new_ssl && \
    git checkout -f sit && \
    mkdir pom_dir && \
    sed -i 's/\r$//' ./start.sh && \
    sed -i 's/\r$//' ./restart.sh && \
    chmod +x ./start.sh && \
    chmod +x ./restart.sh && \
    rm -rf ./src/main/resources/application.yml

ENV M2_HOME=/opt/maven
ENV MAVEN_HOME=/opt/maven
ENV PATH=${M2_HOME}/bin:${PATH}

WORKDIR /usr/local/project/new_ssl
CMD ["./start.sh"]
