FROM openjdk:8-jdk-slim
ENV REPO_HOME=/home/lpr-master-node
RUN export JAVA_HOME && mkdir -p ${REPO_HOME}
COPY target/lpr-master-node-0.0.1.war ${REPO_HOME}
WORKDIR ${REPO_HOME}