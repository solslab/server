FROM khipu/openjdk17-alpine
ARG JAR_FILE=*.jar

COPY build/libs/${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]