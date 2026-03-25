FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]