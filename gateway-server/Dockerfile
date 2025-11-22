FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY target/gateway-server.jar app.jar
COPY start.sh .
RUN chmod +x start.sh

ENTRYPOINT ["./start.sh"]
