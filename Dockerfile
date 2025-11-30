# build stage

FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
CPOY . /app
RUN mvn clean package -DskipTests

# deploy stage

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=buildstage /app/target/job-traker-web-application.jar /app/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/job-traker-web-application.jar"]