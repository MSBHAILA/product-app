FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Add a non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy the jar file
COPY target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 9090

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
