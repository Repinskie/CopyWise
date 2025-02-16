FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/CopyWise-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8091

CMD ["java", "-jar", "app.jar"]