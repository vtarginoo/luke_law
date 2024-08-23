# Estágio de Build
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /app

# Copia o wrapper do Maven e o arquivo de configuração
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x ./mvnw

# Faz o download das dependências do pom.xml para permitir a construção offline
RUN ./mvnw dependency:go-offline -B

# Copia o código-fonte para o container
COPY src src

# Compila o código e cria o JAR
RUN ./mvnw clean install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Estágio de Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Argumentos para as variáveis de ambiente
ARG SPRING_APPLICATION_NAME=mvp-luke-law
ARG SENDGRID_API_KEY
ARG TWILIO_ACCOUNT_SID
ARG TWILIO_AUTH_TOKEN
ARG TWILIO_WHATSAPP_FROM

# Definindo as variáveis de ambiente
ENV SPRING_APPLICATION_NAME=${SPRING_APPLICATION_NAME}
ENV SENDGRID_API_KEY=${SENDGRID_API_KEY}
ENV TWILIO_ACCOUNT_SID=${TWILIO_ACCOUNT_SID}
ENV TWILIO_AUTH_TOKEN=${TWILIO_AUTH_TOKEN}
ENV TWILIO_WHATSAPP_FROM=${TWILIO_WHATSAPP_FROM}

# Definir variável de ambiente para indicar que está rodando em Docker
ENV DOCKER_ENV=true

# Instalar dependências necessárias para o Chrome e o ChromeDriver
RUN apk add --no-cache \
    udev \
    ttf-freefont \
    chromium \
    chromium-chromedriver \
    nss \
    && apk add --no-cache \
    harfbuzz \
    ca-certificates \
    bash \
    && rm -rf /var/cache/apk/*

# Instalar Kafka e Zookeeper
RUN apk add --no-cache curl bash jq docker \
    && mkdir -p /opt/kafka \
    && curl -sSL https://dlcdn.apache.org/kafka/3.8.0/kafka_2.13-3.8.0.tgz | tar -xz -C /opt/kafka --strip-components=1 \
    && apk add openjdk17-jre

# Definir variáveis de ambiente para Chrome e ChromeDriver
ENV CHROME_BIN=/usr/bin/chromium-browser
ENV CHROME_PATH=/usr/lib/chromium/

# Definir variáveis de ambiente para Kafka
ENV KAFKA_HOME=/opt/kafka
ENV PATH=${PATH}:${KAFKA_HOME}/bin

# Copia os arquivos de configuração do Kafka e Log4j
COPY server.properties /opt/kafka/config/server.properties
COPY log4j.properties /opt/kafka/config/log4j.properties

# Copia o JAR do estágio de build
COPY --from=build /app/target/mvp-luke-law-0.0.1-SNAPSHOT.jar app.jar

# Porta da aplicação
EXPOSE 8080
# Kafka
EXPOSE 9092
# Zookeeper
EXPOSE 2181

# Script para iniciar Kafka, Zookeeper e a aplicação
COPY start-kafka.sh /usr/bin/start-kafka.sh
RUN chmod +x /usr/bin/start-kafka.sh

# Executa o aplicativo junto com Kafka e Zookeeper
ENTRYPOINT ["start-kafka.sh"]