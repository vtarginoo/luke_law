#!/bin/bash

# Iniciar o Zookeeper
echo "Iniciando Zookeeper..."
$KAFKA_HOME/bin/zookeeper-server-start.sh -daemon $KAFKA_HOME/config/zookeeper.properties
if [ $? -ne 0 ]; then
  echo "Erro ao iniciar o Zookeeper"
  exit 1
fi
sleep 20  # Aguarda 5 segundos para garantir que o Zookeeper tenha iniciado

# Iniciar o Kafka
echo "Iniciando Kafka..."
$KAFKA_HOME/bin/kafka-server-start.sh -daemon $KAFKA_HOME/config/server.properties
if [ $? -ne 0 ]; then
  echo "Erro ao iniciar o Kafka"
  exit 1
fi
sleep 30  # Aguarda 20 segundos para garantir que o Kafka tenha iniciado