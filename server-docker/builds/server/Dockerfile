FROM openjdk:11-jre
WORKDIR /usr/src/myapp
COPY ./server.jar ./
CMD ["java", "-jar", "server.jar", "-e", "nats://nats:4222", "--host", "db"]
