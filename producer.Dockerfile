FROM maven:3.8.6-openjdk-18-slim as  builder
ENV HOME=/app
RUN mkdir -p $HOME
WORKDIR $HOME

ADD pom.xml $HOME
ADD librabbitmq/pom.xml $HOME/librabbitmq/pom.xml
ADD producer-converter/pom.xml $HOME/producer-converter/pom.xml
ADD consumer-converter/pom.xml $HOME/consumer-converter/pom.xml

RUN mvn -pl librabbitmq verify --fail-never
ADD librabbitmq $HOME/librabbitmq
RUN mvn -pl librabbitmq install

RUN mvn -pl producer-converter verify --fail-never
ADD producer-converter $HOME/producer-converter

RUN mvn -pl consumer-converter verify --fail-never
ADD consumer-converter $HOME/consumer-converter

RUN mvn -pl librabbitmq,producer-converter,consumer-converter clean package install


FROM openjdk:18-alpine
COPY --from=builder /app/consumer-converter/target/*.jar /app/consumer-converter.jar
EXPOSE 8081 5672 15672
ENTRYPOINT java -jar /app/consumer-converter.jar

FROM openjdk:18-alpine
COPY --from=builder /app/producer-converter/target/*.jar /app/producer-converter.jar
EXPOSE 8080 6379
ENTRYPOINT java -jar /app/producer-converter.jar