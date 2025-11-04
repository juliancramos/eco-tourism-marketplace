FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY target/service-catalog.jar app.jar
COPY start.sh .
RUN chmod +x start.sh

ENTRYPOINT ["./start.sh"]
