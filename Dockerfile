# Define a imagem base
FROM openjdk:17
WORKDIR /app
COPY target/spring-boot-application.jar .
CMD ["java", "-jar", "spring-boot-application.jar"]
