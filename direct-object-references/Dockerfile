FROM openjdk:8-jre-alpine
MAINTAINER Dominik Schadow <dominikschadow@gmail.com>

EXPOSE 8080

COPY target/direct-object-references.jar app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]