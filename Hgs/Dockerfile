# -------- BUILD STAGE --------
FROM eclipse-temurin:25-jdk AS build

WORKDIR /build
COPY . .

RUN chmod +x mvnw
RUN ./mvnw -B package -DskipTests

# Debug: show what was built
RUN echo "==== TARGET CONTENT ====" && ls -lah target

# -------- RUN STAGE --------
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copy the JAR that Spring Boot actually created
COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080
ENV PORT=8080

CMD ["sh", "-c", "java -jar app.jar"]
