ARG AWS_ACCOUNT_NUMBER
ARG AWS_REGION
ARG AWS_ECR_BACKEND

FROM openjdk:17.0.2-jdk-oracle

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} application.jar

ENTRYPOINT ["java","-jar","/application.jar"]
