FROM maven:3.9.12-eclipse-temurin-21 AS build

COPY src /app/src

COPY pom.xml /app

WORKDIR /app
RUN mvn clean install -U

FROM eclipse-temurin:21-jdk-alpine
COPY --from=build /app/target/code-assessment-20260113.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]