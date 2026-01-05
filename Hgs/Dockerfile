# 1) Build stage
FROM eclipse-temurin:25-jdk AS builder
WORKDIR /app

# Copy Maven files
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./
RUN chmod +x mvnw

# Copy source
COPY src ./src

# Build the JAR
RUN ./mvnw clean package -DskipTests

# 2) Runtime stage
FROM eclipse-temurin:25-jdk
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
