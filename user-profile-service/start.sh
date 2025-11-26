#!/bin/sh

echo "Esperando a Config Server..."
while ! nc -z config-server 8888; do
  echo "Aún no disponible, reintentando en 3s..."
  sleep 3
done

echo "Esperando a Eureka Server..."
while ! nc -z eureka-server 8761; do
  echo "Aún no disponible, reintentando en 3s..."
  sleep 3
done

echo "Iniciando user-profile-service..."
java -jar app.jar
