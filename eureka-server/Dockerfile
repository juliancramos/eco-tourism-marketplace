FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY target/eureka-server.jar app.jar
COPY start.sh .

RUN chmod +x start.sh

EXPOSE 8761
ENTRYPOINT ["./start.sh"]
