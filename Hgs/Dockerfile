# ---------- BUILD STAGE ----------
FROM eclipse-temurin:25-jdk AS build

WORKDIR /build

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# ---------- RUN STAGE ----------
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copy the actual built JAR
COPY --from=build /build/target/*.jar /app/app.jar

ENV PORT=8080
EXPOSE 8080

CMD ["sh", "-c", "java -jar /app/app.jar"]
