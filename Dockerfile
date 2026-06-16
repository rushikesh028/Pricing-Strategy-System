# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk AS builder
WORKDIR /workspace

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw -q -DskipTests dependency:go-offline

COPY src ./src
RUN ./mvnw -q -DskipTests clean package

FROM eclipse-temurin:17-jre

WORKDIR /app

RUN useradd --system --uid 1001 spring

COPY --from=builder /workspace/target/*.jar /app/app.jar

RUN chown -R spring:spring /app
USER spring

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]