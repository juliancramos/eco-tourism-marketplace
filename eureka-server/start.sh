#!/bin/sh

echo "Esperando a Config Server (config-server:8888)..."

while ! nc -z config-server 8888; do
  echo "Aún no disponible, reintentando en 3 segundos..."
  sleep 3
done

echo "Config Server disponible. Iniciando Eureka Server..."

java -jar app.jar
