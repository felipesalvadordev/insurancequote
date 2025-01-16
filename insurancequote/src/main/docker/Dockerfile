FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} insurancequote.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "insurancequote.jar"]