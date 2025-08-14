###
# Dockerfile usado para consutruir la imagen del coetl
#
# Antes de construir la imagen debemos haber compilado previamente el proyecto (desde el directorio raíz):
#
# mvn clean package -Penv
#
# A continuación podremos construir la imagen ejecutando siempre desde el directorio raíz del proyecto:
#
# docker build -f dockerfiles/Dockerfile -t coetl:<version> .
#
# Para ejecutar un contenedor asociado a esta imagen:
# docker run -i --rm -p 8080:8080 coetl:<version>
#
# Para ejecutar con opciones de debug habilitadas:
# docker run -i --rm \
# -e JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n" \
# -p 8080:8080 \
# -p 5005:5005 \
# coetl:<version>
###
# Usamos imagen de tomcat en lugar de la de java directamente porque no hemos conseguido que la librería overthere
# funcione correctamente cuando ejecutamos con java -jar
# lo cual da errores relativos al protocolo ssh
###
FROM tomcat:9.0.98-jdk8
LABEL MAINTAINER=info@arte-consultores.com

RUN mkdir -p /data/edatos/coetl/conf

COPY dockerfiles/conf /data/edatos/coetl/conf
COPY target/*.war /usr/local/tomcat/webapps/coetl.war

# Definir variables de entorno con ubicaciones externas de configuración
ENV SPRING_CONFIG_LOCATION=file:/data/edatos/coetl/conf/
ENV DATA_LOCATION_CONFIG=file:/data/edatos/coetl/conf/data-location.properties
ENV LANG='es_ES.UTF-8'

EXPOSE 8080

RUN env

CMD ["catalina.sh", "run"]
