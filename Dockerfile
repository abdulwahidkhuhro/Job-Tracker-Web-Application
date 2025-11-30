# build stage

FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
CPOY . /app
RUN mvn clean package -DskipTests

# deploy stage

FROM mosipid/openjdk-21-jre
WORKDIR /app
COPY --from=buildstage /app/target/job-traker-web-application.jar /app/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/job-traker-web-application.jar"]