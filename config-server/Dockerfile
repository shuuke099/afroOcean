# ---- build stage (multi-module aware) ----
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . .
# build only the config-server module (and its deps)
RUN mvn -q -DskipTests -pl config-server -am package

# ---- runtime ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# app jar
COPY --from=build /workspace/config-server/target/*.jar /app/app.jar

# bundle your native config repo into the image (optional)
COPY config-repo /app/config-repo

# Config Server defaults
ENV SPRING_PROFILES_ACTIVE=native
ENV SPRING_CLOUD_CONFIG_SERVER_NATIVE_SEARCH_LOCATIONS=file:/app/config-repo

EXPOSE 8888
ENTRYPOINT ["java","-jar","/app/app.jar"]
