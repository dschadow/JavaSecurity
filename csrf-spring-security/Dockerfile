FROM openjdk:11-jre-slim
MAINTAINER Dominik Schadow <dominikschadow@gmail.com>

EXPOSE 8080

ARG JAR_FILE
ADD target/${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]