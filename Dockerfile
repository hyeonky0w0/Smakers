FROM eclipse-temurin:21-jre
WORKDIR /app

COPY build/libs/*.jar app.jar
COPY resources/application.yml /application.yml

ENTRYPOINT ["java","-jar","/app/app.jar","--spring.config.location=/application.yml"]
