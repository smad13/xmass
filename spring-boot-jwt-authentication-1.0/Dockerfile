# Etapa de construcción
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copiar solo el archivo pom.xml y descargar dependencias
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline

# Copiar el código fuente después de descargar dependencias
COPY src src

# Construir la aplicación
RUN ./mvnw clean package -DskipTests

# Segunda etapa: Crear imagen final más liviana
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiar el JAR generado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
