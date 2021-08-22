FROM openjdk:11

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} card-api.jar

ENTRYPOINT ["java","-jar","/card-api.jar"]