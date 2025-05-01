# Use Eclipse Temurin base image with Java 17
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy Maven wrapper and POM file
COPY mvnw* pom.xml ./
COPY .mvn .mvn

# Copy the rest of the app
COPY src ./src

# Download dependencies + package the application
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Run the application
CMD ["./mvnw", "spring-boot:run"]
