FROM asia.gcr.io/new-life-experience/openjdk:17.0.2-jdk-oracle
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java","-jar","/application.jar"]
