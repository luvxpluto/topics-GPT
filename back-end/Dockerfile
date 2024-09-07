# Usar una imagen base oficial de Java
FROM openjdk:21-slim as build

# Instalar Maven y otras dependencias necesarias
RUN apt-get update && \
    apt-get install -y maven

# Establecer el directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo pom.xml y descargar las dependencias
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copiar el resto del código fuente de la aplicación
COPY src ./src

# Construir la aplicación para producción
RUN mvn package -DskipTests

# Usar una imagen de Java para ejecutar la aplicación
FROM openjdk:17-slim

# Copiar el ejecutable desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto que usará la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]
