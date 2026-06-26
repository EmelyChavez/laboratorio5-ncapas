# ---------- Etapa 1: build ----------
# Descarga una imagen del jdk 21 y a esta etapa la llama build
FROM eclipse-temurin:21-jdk-alpine AS build
# Dentro del contenedor crea una carpeta llamada app
WORKDIR /app

# Copiamos lo necesario para instalar las dependencias
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN chmod +x mvnw
# Ejecuta el comando para instalar las dependencias
RUN ./mvnw dependency:go-offline -B

# Copiamos el codigo fuente y se compila :D
COPY src ./src
# Nos saltamos los test ya que de eso se encarga CI :D
RUN ./mvnw clean package -DskipTests -B

# ---------- Etapa 2: runtime ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crea un usuario sin privilegios para correr el proyecto
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copia el .jar que se obtuvo en la etapa 1 (build)
COPY --from=build /app/target/*.jar app.jar

#Exponemos el puerto
EXPOSE 8080

# Comando que se va a ejecutar cuando se levante el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]