# Etapa de construcción
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copiar archivos necesarios para la compilación
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline

# Copiar código fuente después de descargar dependencias
COPY src ./src

# Construir la aplicación
RUN ls -l src && ./mvnw clean package -DskipTests

# Segunda etapa: Crear imagen final más liviana
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiar el JAR generado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
