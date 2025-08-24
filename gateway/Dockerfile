# --- builder ---
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
# build ONLY the gateway module (plus deps)
RUN mvn -pl gateway -am clean package -DskipTests

# --- runtime ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/gateway/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
