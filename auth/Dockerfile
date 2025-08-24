# --- build only the auth module ---
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . .
# Build only 'auth' and its dependencies (-pl auth -am)
RUN mvn -q -DskipTests -pl auth -am package

# --- runtime ---
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy the fat jar from the auth module
COPY --from=build /workspace/auth/target/*.jar /app/app.jar

EXPOSE 8001
ENTRYPOINT ["java","-jar","/app/app.jar"]
