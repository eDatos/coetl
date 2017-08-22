# CAS SERVER
En este directorio se incluyen los archivos necesarios para construir una imagen Docker de un Servidor CAS. El proceso consiste en bajar el código más
reciente del proyecto `cas-overlay-template` del Git-Hub oficial de Apereo y construir la imagen desde el código anexando las configuraciones de
CAS que se deseen para nuestro desarrollo.

Extender el proyecto `cas-overlay-template` es la forma oficial recomendada para instalar una versión de CAS independientemente de si se realiza
con un contenedor Docker.

Esta instancia de CAS está configurada para ser usada contra un LDAP, por tanto se añaden las dependencias oportunas y la configuración. El ldap para el
que está configurado es [LAM_ARTE][]

### Configuración para entorno DEV

Es posible definir una configuración específica para el entorno de desarrollo (dev) en el caso que difiera del de producción, por ejemplo, para añadir credenciales que no deban ser públicas o añadidas al repositorio.
Para ello hay que modificar el fichero `cas/config/cas-dev.properties` con la configuración que queramos reemplazar y a la hora de construir la imagen pasarle un parámetro extra `--build-arg DEV=true`



### Sin soporte SSL

En CAS, si se desea soporte Single Sign On (SSO), para permitir login compartido entre distintas aplicaciones, necesitamos habilitar SSL. Esto conlleva entre otras
cosas, la necesidad de crear un certificado y almacenarlo en un TrusStore que usará el servidor embebido que levante CAS. Además será necesario importar el certificado
en la máquina Java que ejecuta la aplicación que se está desarrollando a fin de ejecutar el handshake correctamente. Durante el desarrollo es posible que el desarrollador
no necesite SSO y por simplicidad (evitar estar creando los certificados), prefiera usar HTTP. En este caso hay que asegurarse que en el archivo 
`/etc/cas/etc/cas/config/cas.properties` están descomentadas las propiedades `# FOR HTTP` y comentadas las propiedades `# FOR HTTPS`.
A continuación, saltar al apartado **Construcción y ejecución**


### Con soporte SSL

Si se desea soporte Single Sign On (SSO), para permitir login compartido entre distintas aplicaciones, es necesario habilitar SSL en el servidor embebido de CAS. En este caso 
hay que asegurarse que en el archivo `/etc/cas/etc/cas/config/cas.properties` están descomentadas las propiedades `# FOR HTTPS` y comentadas las propiedades `# FOR HTTP`.
A continuación se debe de serguir los siguientes pasos:
 
* En primer lugar debemos crear un KeyStore que nos permita usar SSL en el servidor CAS. Los password para el store y el certificado deben ser `changeit`.
Por defecto ya se ha creado un keystore en este proyecto por lo que no será necesario crearlo salvo que se haya caducado (año 2044) o cambian el nombre de la máquina "docker-machine". No 
obstante un ejemplo de creación es el siguiente:

	keytool -genkey -alias cas -keyalg RSA -validity 9999 -keystore W:/ECIT/2017-secretaria/01-proyecto/04-git/secreataria-libro/etc/docker/cas/etc/cas/thekeystore
	
	Introduzca la contraseña del almacén de claves:
	Volver a escribir la contraseña nueva:
	¿Cuáles son su nombre y su apellido?
	  [Unknown]:  docker-machine
	¿Cuál es el nombre de su unidad de organización?
	  [Unknown]:  Arte Consultores S.L. DEV
	¿Cuál es el nombre de su organización?
	  [Unknown]:  Arte Consultores S.L. DEV
	¿Cuál es el nombre de su ciudad o localidad?
	  [Unknown]:  Tenerife
	¿Cuál es el nombre de su estado o provincia?
	  [Unknown]:  Canarias
	¿Cuál es el código de país de dos letras de la unidad?
	  [Unknown]:  ES
	¿Es correcto CN=Arte Consultores S.L. DEV, OU=Arte Consultores S.L. DEV, O=Arte Consultores S.L. DEV, L=Tenerife, ST=Canarias, C=ES?
	  [no]:  si
	
	Introduzca la contraseña de clave para <cas>
	        (INTRO si es la misma contraseña que la del almacén de claves):
	Volver a escribir la contraseña nueva:

 El KeyStore generado hay que ubicarlo en `/etc/cas/etc/cas/thekeystore`

### Construcción y ejecución

* A continuación crearemos la imagen que usaremos en Docker. Para ello debemos de tener Docker instalado en el sistema (tanto si es de forma nativa como si es a través de la docker-machine). Por 
ejemplo, en Window sin instalación nativa, usando Docker Toolbox, abrimos una consola `Docker Quick Install` como siempre para tener Docker en el Path. Si tenemos Docker Nativo con abrir una consola normal que tenga Docker en el Path es suficiente.
```
	$ cd /w/AGRICULTURA/2017-secretaria/01-proyecto/04-git/secretaria-libro/etc/docker/cas
	$ docker build -t secretaria/cas .
```

* Lo ejecutamos

	docker run -d -p 9080:8080 -p 9443:8443 --name="cas-secretaria" secretaria/cas

> NOTA: Si al levantar el contenedor este no arranca (en los logs s muestra un error de tipo "exec") puede ser debido a que Windows a convertido el fichero "run-cas.sh". Debemos de convertirlo a UNIX
por ejemplo, con la utilidad "dos2unix" (disponible en cmder). Ejemplo "dos2unix run-cas.sh"

* Si configuramos un servidor sin SSL, podemos acceder con HTTP:

	http://docker-machine:9080/cas

* Si configuramos un servidor con SSL, podemos acceder con HTTPS:

	https://docker-machine:9443/cas

* A la hora de trabajar con el CAS, si hemos elegido la opción SSL, será necesario importar el certificado creado anteriormente en la máquina de JAVA que ejecuta la aplicación que estamos desarrollando:t
Para ello hay que ir a la ruta de instalación del JAVA en cuestión y ejecutar un comando similar a:

`bin\keytool.exe -importcert -alias secretaria_ssl -keystore jre/lib/security/cacerts -storepass changeit -file "C:\dev\proyectos\AGRICULTURA\2017-secretaria\01-proyecto\04-git\secretaria-libro\etc\docker\cas\cas.cer"`



[LAM_ARTE]: http://lam.arte-consultores.com