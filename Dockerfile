# Usar la imagen base de Ubuntu
FROM ubuntu:latest

# Establecer variables de entorno
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-8u76
ENV PATH $JAVA_HOME/bin:$PATH

# Instalar dependencias
RUN apt-get update && \
    apt-get install -y wget tar && \
    apt-get clean

# Descargar e instalar JDK 8u76
RUN wget --no-verbose --no-check-certificate \
    -O /tmp/jdk-8u76-linux-x64.tar.gz \
    "https://download.oracle.com/otn-pub/java/jdk/8u76-b17/jdk-8u76-linux-x64.tar.gz" && \
    mkdir -p $JAVA_HOME && \
    tar -xzf /tmp/jdk-8u76-linux-x64.tar.gz -C $JAVA_HOME --strip-components=1 && \
    rm /tmp/jdk-8u76-linux-x64.tar.gz

# Crear un directorio para la aplicación
RUN mkdir /app

# Establecer el directorio de trabajo
WORKDIR /app

# Comando por defecto para abrir una shell
CMD ["/bin/bash"]
