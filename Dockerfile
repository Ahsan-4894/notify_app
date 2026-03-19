# ---------- BUILD STAGE ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy dependency descriptor first (for caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

#Copy source code
COPY src ./src

# Build jar
RUN mvn clean package -DskipTests

# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:21-jre-jammy as runtime
WORKDIR /app
COPY --from=build /app/target/crud-api.jar crud-api.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "crud-api.jar"]