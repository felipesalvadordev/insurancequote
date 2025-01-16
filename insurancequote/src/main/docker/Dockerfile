FROM openjdk:17
WORKDIR /insurancequote
COPY target/insurancequote-0.0.1-SNAPSHOT.jar insurancequote.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "insurancequote.jar"]