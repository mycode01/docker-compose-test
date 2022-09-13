FROM adoptopenjdk/openjdk11
EXPOSE 8761
ARG JAR_FILE=build/libs/discovery-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-XX:MaxRAMPercentage=80","-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=UTC","-jar","/app.jar"]