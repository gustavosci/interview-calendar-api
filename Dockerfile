FROM openjdk:11-jre-slim

RUN mkdir /opt/app
ADD build/libs/calendar-interview-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar", "--java.security.egd=file:/dev/./urandom"]