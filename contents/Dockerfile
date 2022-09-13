FROM adoptopenjdk/openjdk11
EXPOSE 8082
ARG JAR_FILE=build/libs/contents-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-XX:MaxRAMPercentage=80","-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=UTC","-jar","/app.jar"]