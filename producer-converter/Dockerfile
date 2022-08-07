FROM openjdk:18-alpine

MAINTAINER lucas

ADD target/producer-converter-*.jar /app.jar

EXPOSE 8080 6379

ENTRYPOINT ["java", "-jar", "/app.jar"]

