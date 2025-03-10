# Utiliza una imagen base con Java (ajusta la versión si es necesario)
FROM openjdk:11-jre-slim

# Copia el archivo JAR generado al contenedor y lo renombra a app.jar
COPY target/demo-jwt-0.0.1-SNAPSHOT.jar app.jar

# Indica el comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app.jar"]
