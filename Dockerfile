# Use an official Maven image with Java 17 as the base environment
FROM maven:3.9.6-eclipse-temurin-17

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download dependencies to pre-cache them
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of the project source code
COPY . .

# Default command to execute the test suite
CMD ["mvn", "test"]