
# ---- Build Stage ----

FROM maven:3.9.6-eclipse-temurin-17 AS buildstage

WORKDIR /app

COPY . /app

RUN mvn clean package -DskipTests

# ---- Runtime Stage ----

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=buildstage /app/target/job-traker-web-application.jar /app/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/job-traker-web-application.jar"]


