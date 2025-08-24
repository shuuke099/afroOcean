# --- builder ---
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
# build ONLY the reviews module (and its deps)
RUN mvn -pl reviews -am clean package -DskipTests

# --- runtime ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/reviews/target/*.jar app.jar
EXPOSE 8006
ENTRYPOINT ["java","-jar","app.jar"]
