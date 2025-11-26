FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY target/user-profile-service.jar app.jar
COPY start.sh .

RUN chmod +x start.sh

EXPOSE 8081
ENTRYPOINT ["./start.sh"]
