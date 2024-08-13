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

# Copia o JAR do estágio de build
COPY --from=build /app/target/indicadores-disponibilidade-pje-0.0.1-SNAPSHOT.jar app.jar

# Porta da aplicação
EXPOSE 8080

# Executa o aplicativo
ENTRYPOINT ["java", "-jar", "app.jar"]