#!/bin/sh

echo "Esperando a Config Server en config-server:8888..."
while ! nc -z config-server 8888; do
  sleep 2
done

echo "Esperando a Eureka Server en eureka-server:8761..."
while ! nc -z eureka-server 8761; do
  sleep 2
done

echo "Iniciando Service Catalog..."
java -jar app.jar
