#!/bin/bash

# Parar o Kafka
$KAFKA_HOME/bin/kafka-server-stop.sh

# Parar o Zookeeper
$KAFKA_HOME/bin/zookeeper-server-stop.sh
