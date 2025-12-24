FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY mvnw .
COPY .mvn .mvn

RUN ./mvnw clean package

EXPOSE 8082

CMD ["java", "-jar", "target/finalexam-0.0.1-SNAPSHOT.jar"]
