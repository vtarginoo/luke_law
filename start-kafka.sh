#!/bin/bash

# Iniciar o Zookeeper
$KAFKA_HOME/bin/zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties &
ZOOKEEPER_PID=$!

# Esperar o Zookeeper iniciar
sleep 5

# Iniciar o Kafka
$KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties &
KAFKA_PID=$!

# Iniciar a aplicação Java
java -jar /app/app.jar &
APP_PID=$!

# Esperar a aplicação Java terminar
wait $APP_PID

# Se a aplicação Java terminar, encerrar Kafka e Zookeeper
kill $KAFKA_PID
kill $ZOOKEEPER_PID
