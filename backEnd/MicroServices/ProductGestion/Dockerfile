FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/productgestion-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]