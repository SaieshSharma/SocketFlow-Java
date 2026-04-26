FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
RUN mkdir logs
COPY MultiThreadedServer.java ./
RUN javac MultiThreadedServer.java
EXPOSE 2000
CMD ["java", "MultiThreadedServer"]