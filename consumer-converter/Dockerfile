FROM openjdk:18-alpine

MAINTAINER lucas

ADD target/consumer-converter-*.jar /app.jar

EXPOSE 8081 5672

ENTRYPOINT ["java", "-jar", "/app.jar"]