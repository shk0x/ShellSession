# docker build -t openjdk8-ppa .
# docker run -it openjdk8-ppa
#
# Usar la imagen base de Ubuntu
FROM ubuntu:20.04

# Establecer variables de entorno
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH

# Instalar dependencias y OpenJDK 8
RUN apt-get update && \
    apt-get install -y software-properties-common && \
    add-apt-repository ppa:openjdk-r/ppa && \
    apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get clean

# Crear un directorio para la aplicación
RUN mkdir /app

# Establecer el directorio de trabajo
WORKDIR /app

# Comando por defecto para abrir una shell
CMD ["/bin/bash"]
