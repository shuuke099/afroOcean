# --- builder ---
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
# build only the products module (and its deps)
RUN mvn -pl products -am clean package -DskipTests

# --- runtime ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
# copy the built jar from the products module
COPY --from=builder /app/products/target/*.jar app.jar
EXPOSE 8002
ENTRYPOINT ["java","-jar","app.jar"]
