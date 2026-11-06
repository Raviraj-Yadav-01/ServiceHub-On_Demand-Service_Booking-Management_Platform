FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Local PC se pre-built jar copy karein
COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]


