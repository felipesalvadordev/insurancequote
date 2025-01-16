FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/*.jar /app/insurancequoteapp.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/insurancequoteapp.jar"]