# Step 1: Use an official lightweight Java runtime base image
FROM eclipse-temurin:17-jre-alpine

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the compiled JAR file from your host machine into the container
COPY transport-new-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Expose the port the app runs on
EXPOSE 8080

# Step 5: Define the command to execute the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]