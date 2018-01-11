FROM gradle:latest

USER root
RUN mkdir /opt/artifacts && chown -R gradle:gradle /opt/artifacts
RUN apt update && apt install -y ssh
RUN systemctl enable ssh
USER gradle
