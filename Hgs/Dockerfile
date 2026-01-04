# ---- Build stage (Java 25) ----
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src src

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# ---- Run stage (Java 25) ----
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

CMD ["sh", "-c", "java -jar app.jar"]
