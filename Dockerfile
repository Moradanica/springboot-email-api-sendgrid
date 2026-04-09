# Use official Eclipse Temurin OpenJDK 17
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy Maven files first (for dependency caching)
COPY pom.xml .
COPY src ./src

# Install Maven, build the JAR
RUN apt-get update && apt-get install -y maven \
    && mvn clean package -DskipTests

# Find the generated JAR automatically (no need to hardcode)
RUN ls target/   # optional: check which JAR was created
RUN cp target/*.jar app.jar

# Expose port
# EXPOSE 8080

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]