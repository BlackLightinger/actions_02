FROM openjdk:21-jdk-slim

ARG GRADLE_VERSION=8.3
RUN apt-get update && apt-get install -y curl unzip && \
    curl -s https://downloads.gradle-dn.com/distributions/gradle-${GRADLE_VERSION}-bin.zip -o gradle.zip && \
    unzip gradle.zip -d /opt && \
    rm gradle.zip && \
    ln -s /opt/gradle-${GRADLE_VERSION}/bin/gradle /usr/bin/gradle

WORKDIR /app

COPY . .

RUN gradle dependencies --no-daemon

CMD ["gradle", "--no-daemon", "test"]
