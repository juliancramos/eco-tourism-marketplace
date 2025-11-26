FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/payment-service.jar app.jar
COPY start.sh .
RUN chmod +x start.sh
ENTRYPOINT ["./start.sh"]
