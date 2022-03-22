FROM maven:3-amazoncorretto-17 as build
WORKDIR /bartenderly
ADD ./ ./
RUN mvn clean package

FROM openjdk:17-ea-jdk-oracle
COPY --from=build app/target/*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar"]