# --- builder ---
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
# build ONLY the payments module (and its deps)
RUN mvn -pl payments -am clean package -DskipTests

# --- runtime ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/payments/target/*.jar app.jar
EXPOSE 8004
ENTRYPOINT ["java","-jar","app.jar"]
