FROM khipu/openjdk17-alpine
ARG JAR_FILE=*.jar

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]