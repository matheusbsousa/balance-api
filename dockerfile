# Use a smaller base image for the runtime
FROM openjdk:21-jdk-slim

# WORKDIR /app

# Copy the built jar from the build stage
COPY build/libs/balance-api-v1-01.jar .
# COPY --from=build /app/build/libs/balance-api-v1-01.jar .

# Run the application
ENTRYPOINT ["java", "-jar", "balance-api-v1-01.jar"]