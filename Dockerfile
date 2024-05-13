FROM openjdk:17
ARG JAR_FILE=build/libs/tripsync-0.0.1.jar
COPY ${JAR_FILE} ./app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "./app.jar"]
