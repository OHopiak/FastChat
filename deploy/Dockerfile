FROM gradle:latest

ENV GRADLE_BUILD_DIR /home/gradle/workspace

COPY . ${GRADLE_BUILD_DIR}

USER root
RUN chown gradle:gradle -R ${GRADLE_BUILD_DIR}
USER gradle

WORKDIR ${GRADLE_BUILD_DIR}
