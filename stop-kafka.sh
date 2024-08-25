#!/bin/bash

# Parar o Kafka
echo "Parando Kafka..."
$KAFKA_HOME/bin/kafka-server-stop.sh
if [ $? -ne 0 ]; then
  echo "Erro ao parar o Kafka"
  exit 1
fi
sleep 5  # Aguarda 5 segundos para garantir que o Kafka tenha parado

# Parar o Zookeeper
echo "Parando Zookeeper..."
$KAFKA_HOME/bin/zookeeper-server-stop.sh
if [ $? -ne 0 ]; then
  echo "Erro ao parar o Zookeeper"
  exit 1
fi
sleep 5  # Aguarda 5 segundos para garantir que o Zookeeper tenha parado

echo "Kafka e Zookeeper foram parados com sucesso."
