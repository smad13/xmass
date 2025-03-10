# Usa una imagen base de Java
FROM eclipse-temurin:17-jdk AS build

# Establece el directorio de trabajo
WORKDIR /app

# Copia los archivos del proyecto
COPY . .

# Construye la aplicación
RUN ./mvnw clean package -DskipTests

# Segunda etapa: Crear imagen final más liviana
FROM eclipse-temurin:17-jre

# Establece el directorio de trabajo
WORKDIR /app

# Copia el JAR generado en la primera etapa
COPY --from=build /app/target/demo-jwt-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]
