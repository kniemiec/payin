FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/application.jar"]
