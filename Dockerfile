FROM khipu/openjdk17-alpine
ARG JAR_FILE=*.jar

# JAR 파일 복사
COPY build/libs/${JAR_FILE} app.jar

# Spring Boot에 설정 파일의 위치를 알려주기
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]