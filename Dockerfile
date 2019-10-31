FROM openjdk:11-jre-slim

RUN mkdir /app

WORKDIR /app

ADD target/media-1.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "media-1.0-SNAPSHOT.jar"]
