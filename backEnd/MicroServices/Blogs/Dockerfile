FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/blogs-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090  # Assuming a port; adjust if specified elsewhere
ENTRYPOINT ["java", "-jar", "app.jar"]