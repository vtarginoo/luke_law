# Estágio de Build
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x ./mvnw
# Faça o download das dependencias do pom.xml
RUN ./mvnw dependency:go-offline -B

COPY src src

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
# Definindo as variáveis de ambiente
ENV SPRING_APPLICATION_NAME=${SPRING_APPLICATION_NAME}
ENV SENDGRID_API_KEY=${SENDGRID_API_KEY}
ENV TWILIO_ACCOUNT_SID=${TWILIO_ACCOUNT_SID}
ENV TWILIO_AUTH_TOKEN=${TWILIO_AUTH_TOKEN}
ENV TWILIO_WHATSAPP_FROM=${TWILIO_WHATSAPP_FROM}


# Copia o JAR do estágio de build
COPY --from=build /app/target/mvp-luke-law-0.0.1-SNAPSHOT.jar app.jar

# Porta da aplicação
EXPOSE 8080

# Executa o aplicativo
ENTRYPOINT ["java", "-jar", "app.jar"]