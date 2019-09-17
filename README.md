
# Consola de ETL

En este tutorial se explica el proceso a seguir para llevar a cabo el proceso de instalación de la aplicación. Se recomienda llevar a cabo una lectura rápida de todo el tutorial antes de comenzar con el proceso de instalación.
Además, podrá encontrar otros anexos de utilidad en los que se detallan algunas cuestiones relacionadas con el desarrollo, los diferentes perfiles de compilación,...

----------

## Introducción

### Descripción de la aplicación
Consola de ETL es la solución corporativa del Gobierno de Canarias para registrar y ejecutar ficheros ETLs desarrollados mediante la tecnología Pentaho Data Integration, permitiendo incluso programar las ejecuciones de estos procesos y la revisión de sus ejecuciones mediante un histórico.

La aplicación está basada en tecnologías web estándar (HTML5, CSS3, Javascript y Java, ver [Más Información](#M%C3%A1s-informaci%C3%B3n)). La implantación de la solución permite llevar a cabo las siguientes tareas: 

* Manejar los usuarios y roles con acceso a la aplicación.
* Registrar, ejecutar y programar procesos ETL.
* Revisar el histórico de ejecuciones de un proceso ETL.

### Requerimientos previos

#### Requisitos del entorno
En este apartado se especifican los requisitos necesarios, referidos al entorno, para que la aplicación funcione adecuadamente:
- Apache Tomcat.  8.5
- Java. 1.8.x
- PostgreSQL.  **TODO**


#### Dependencias
La aplicación requiere de determinados servicios para poder estar completamente operativa. Algunos de ellos son necesarios de manera directa y otros de manera indirecta:
Los servicios necesarios de manera directa (son atacados directamente por la aplicación):
- CAS. Se utiliza para llevar a cabo las labores de autenticación.
- LDAP. Se utiliza para validar las credenciales de los usuarios.
- Pentaho Data Integration Server. Se utiliza para ejecutar los procesos ETL desarrollados en Pentaho.


### Componentes de la instalación
La plantilla consta de una aplicación con interfaz web y un servicio web REST.

### Gestión de roles y permisos

* :construction: **_TODO_**

----------

## Procedimiento de instalación desde cero

### Paso 1. Configuración del entorno
- Configuración del servidor de aplicaciones según los requisitos del entorno ya especificados.
- Configuración de la base de datos. 
    1. La aplicación cuenta con un mecanismo para llevar a cabo la gestión de los cambios de base de datos de manera automatizada (liquidbase).
    2. A priori lo único que es necesario es crear en la aplicación el esquema de base de datos que se va a utilizar. El esquema debe recibir el nombre de "TODO".
    3. Por otra parte, será necesario que el usuario que use la aplicación tenga permisos para la creación de objetos sobre el esquema anterior.
    4. De esta forma la aplicación, en el momento de arrancar, llevará a cabo la creación de todos los objetos que sean necesarios sobre la base de datos.

### Paso 2. Despliegue en servidor de aplicaciones.
- Ubicar el archivo *.war compilado con el perfil adecuado en el servidor de aplicaciones. Se pueden obtener más detalles en el [Anexo de perfiles de compilación](#anexo-perfiles-de-compilaci%C3%B3n). 
- Definir si se desea externalizar la configuración de la aplicación o mantener dentro del propio archivo war.
    1. Si no se desea externalizar la configuración de las propiedades a un directorio DATA se puede omitir este paso.
    2. Si se desea externalizar la configuración de las propiedades a un directorio DATA se tendrán que realizar los siguientes pasos:
        - Se hará una copia del archivo **coetl.war/WEB-INF/classes/config/application-env.yml** a un directorio externalizado de su elección.
        - Se editará el archivo **coetl.war/WEB-INF/classes/config/data-location.properties** y se especificará la ruta anterior.
- Se cumplimentarán las propiedades del fichero **application-env.yml**. Se puede consultar el detalle sobre cada una de las propiedades en el [Anexo de descripción de propiedades de configuración](#anexo-descripci%C3%B3n-de-las-propiedades-de-configuraci%C3%B3n).


### Paso 3. Configuración de logging.
Pueden modificarse los ficheros relacionados con la configuración de logging en **coetl.war/WEB-INF/classes/logback.xml**.

### Paso 4. Arrancar la aplicación.

### Paso 5. Crear usuario administador
Para poder acceder a la aplicación __*es necesario dar de alta un usuario*__.  A continuación, se enumerarán y  explicarán los procesos a realizar para llevar a cabo esta tarea:

1. Crear un usuario y asignarle un rol, para ello es necesario ejecutar la siguiente instrucción SQL:

   ```sql
   -- Crea un usuario y se le asigna un rol existente
   SELECT add_usuario_with_existing_rol('RELLENAR_USUARIO_LDAP', 'RELLENAR_NOMBRE_USUARIO', 'RELLENAR_PRIMER_APPELLIDO_USUARIO', 'RELLENAR_SEGUNDO_APPELLIDO_USUARIO', 'RELLENAR_CORREO_ELECTRONICO_USUARIO', 'RELLENAR_CODIGO_ROL_EXISTENTE');
   ```

Una vez generado este usuario administrador (con todos los permisos), este tendrá los permisos necesario para dar de alta el resto de usuarios mediante la aplicación, en la sección de **Gestión de usuarios**.

>  **Importante:** Existe un fichero script SQL sobre el que basarse para realizar las acciones anteriores. Dicho fichero se encuentra en: [etc/db/01-configuraciones/01-insercion-roles-y-usuarios.sql](etc/db/01-configuraciones/01-insercion-roles-y-usuarios.sql "Fichero de inserción de roles y usuarios").

----------

## Procedimiento de actualización desde versiones anteriores

### Desde versión 1.0.0 a x.y.z

----------

## Anexo. Descripción de las propiedades de configuración
- `spring.datasource.url`
   - Cadena de conexión a la base de datos.
- `spring.datasource.username`
   - Nombre del usuario de conexión a la base de datos.
- `spring.datasource.password`
   - Password de conexión a la base de datos.
- `spring.jpa.show-sql`
   - Permite añadir al log las sentencias SQL.
- `spring.mail.host`
   - Host del servidor para el envío del mail.
- `spring.mail.port`
   - Puerto del servidor para el envío del mail.
- `spring.mail.username`
   - Nombre del usuario para el envío del mail.
- `spring.mail.password`
   - Contraseña del usuario especificado anteriormente.
- `jhipster.mail.from`
   - Cuenta desde la que se quiere especificar que se envían los e-mails.
- `jhipster.mail.base-url`
   - URL de acceso a la aplicación. Esta URL se usará para enviarla por correo a los nuevos usuarios que sean dados de alta en la aplicación.
- `pentaho.endpoint`
   - Endpoint donde se localiza el servidor de Pentaho
- `pentaho.auth.user`
   - Usuario para conectar con el servidor Pentaho.
- `pentaho.auth.password`
   - Contraseña del usuario del servidor Pentaho. 
- `pentaho.host.os`
   - Sistema operativo donde se ha instalado el servidor Pentaho, permite los valores UNIX (incluye Mac OS) o WINDOWS. 
- `pentaho.host.address`
   - Dirección del sistema donde se ha instalado el servidor Pentaho. 
- `pentaho.host.username`
   - Usuario de conexión al servidor donde se ha instalado el servidor Pentaho. 
- `pentaho.host.password`
   - Contraseña del usuario de conexión al servidor donde se ha instalado el servidor Pentaho. 
- `pentaho.host.sudoUsername`
   - Usuario SUDO en el servidor donde se ha instalado el servidor Pentaho. 
- `pentaho.host.sudoPassword`
   - Constraseña del usuario SUDO en el servidor donde se ha instalado el servidor Pentaho. 
- `pentaho.host.sudoPasswordPromptRegex`
   - Expresión regular para detectar la solicitud de password del usuario SUDO que se muestra en el PROMPT del servidor donde se ha instalado el servidor Pentaho, por ejemplo `.*[Pp]assword.*:`:  
- `pentaho.host.sftpPath`
   - Ruta de subida de fichero al servidor donde se ha instalado el servidor Pentaho, por ejemplo `/tmp`. 
- `pentaho.host.resourcesPath`
   - Ruta donde se encuentran los ficheros de recurso adjuntos de las ETLs en el servidor donde se ha instalado el servidor Pentaho, ejemplo `/servers/pentaho/data-integration/resources`. 
- `pentaho.host.ownerUserResourcesPath`
   - Usuario propietario de la ruta donde se encuentran los ficheros de recurso adjuntos de las ETLs en el servidor donde se ha instalado el servidor Pentaho.
- `pentaho.host.ownerGroupResourcesPath`
   - Grupo propietario de la ruta donde se encuentran los ficheros de recurso adjuntos de las ETLs en el servidor donde se ha instalado el servidor Pentaho. 
- `application.cas.endpoint`
   - Endpoint donde se localiza el CAS.
- `application.cas.service`
   - URL absoluta del endpoint de la aplicación donde se va a validar el usuario tras la autenticación en el CAS ('login/cas'). Si la URL de la aplicación es http://miaplicacion.com, esta propiedad debe tomar el valor http://miaplicacion.com/login/cas.
- `application.cas.login`
   - URL a la que se debe acceder para realizar la acción de login. Sólo debe cumplimentarse en el caso se que su valor sea distinto a `application.cas.endopoint`+ '/login'.
- `application.cas.logout`
   - URL a la que se debe acceder para realizar la acción de logout. Sólo debe cumplimentarse en el caso se que su valor sea distinto a `application.cas.endopoint`+ '/logout'.
- `application.cas.validate`
   - URL a la que se debe acceder para realizar la acción de logout. Sólo debe cumplimentarse en el caso se que su valor sea distinto a `application.cas.endopoint`+ '/logout'.
- `debug`
   - Permite aumentar el nivel de log a DEBUG.    
- `application.ldap.url`
   - URL del servidor LDAP. Ejemplo: ldap://ldap.miorganizacion.com
- `application.ldap.username`
   - Usuario que se usa para conectarse al servidor LDAP. Ejemplo: cn=username,dc=miorganizacion,dc=com
- `application.ldap.password`: 
   - Contraseña del usuario LDAP. 
- `application.ldap.base`
   - Ruta relativa dónde se realizarán las operaciones. Ejemplo ou=usuarios,dc=miorganizacion,dc=com
- `application.ldap.searchUsersProperty`
   - Propiedad de LDAP por la que se buscará el usuario mediante su _username_. Valores admitidos: sAMAccountName, cn, uid
- `application.installation.type`
   - Propiedad para indicar el tipo de instalación de la aplicación, permite dos valores (**INTERNAL** / **EXTERNAL**). Dependiendo del valor elegido la aplicación tendrá un estilo y nombre de aplicación diferente. En caso de seleccionar **INTERNAL** la aplicación se mostrará como *"Consola de ETL - Gestión"*, en caso contrario será *"Consola de ETL"*.


## Anexo. Perfiles de compilación

La aplicación se compila con [Maven][], por lo que para poder compilar la misma debemos tener instalado en nuestro entorno esta herramienta.

A la hora de compilar la aplicación podemos especificar un perfil. En función del perfil que especifiquemos, la aplicación se compilará para ser instalada en un servidor de aplicaciones real, o para ser usada en un entorno de desarrollo.

Existen dos perfiles de compilación que podemos especificar según el entorno: `dev` para entorno de desarrollo y `env` para entornos de producción.

En la aplicación existirá un archivo general `application.yml` en el que se ubicarán todas las propiedades de configuración comunes a todos los perfiles y que rara vez se editan.

Para configurar cada entorno, también existirán archivos específicos con las propiedades que pueden ser modificadas por el usuario y dependientes del entorno, teniendo así un fichero `application-env.yml` para configurar las propiedades del entorno de producción, y un `application-dev.yml` para el entorno de desarrollo.

Cuando compilemos el proyecto, se usará una configuración u otra en función del perfil indicado durante la compilación. Hay que tener en cuenta que, en el caso de que una misma propiedad exista tanto en el fichero general `application.yml` como en el dependiente del entorno, tendrá preferencia el valor configurado en el fichero de configuración del entorno.

Para proceder a instalar la aplicación en un servidor de aplicaciones, debemos compilar la misma con el perfil de producción. Sin embargo, si vamos a proceder a modificar la aplicación en nuestro entorno de desarrollo, bastará con que compilemos la aplicación con el perfil de desarrollo. 

Para compilar el proyecto con el perfil de producción es necesario ejecutar lo siguiente:

```bash
mvn clean install -Penv
```

Por otro lado, para compilar el proyecto con el perfil de desarrollo es necesario ejecutar:

```bash
mvn clean install -Pdev
```

Por defecto, si no se especifica un perfil, la aplicación se compila con el perfil de desarrollo `dev`.

Hay que tener presente que si la aplicación se compila con el perfil de desarrollo, para terminar de construir la misma y poder ejecutarla es necesario seguir los pasos descritos en el [Anexo de desarrollo](#anexo-desarrollo).

## Anexo. Desarrollo

A continuación se describen los pasos a seguir para configurar el entorno de desarrollo sobre el cual podamos arrancar y modificar la aplicación.

En primer lugar, tal y como se describe en el [Anexo Perfiles de compilación](#anexo-perfiles-de-compilaci%C3%B3n), debemos compilar la aplicación con Maven.  Para trabajar en un entorno de desarrollo, basta que la compilemos con el perfil `dev`.

A continuación, para terminar de construir la aplicación, es necesario instalar las siguientes dependencias:

1. [Node.js][]: Lo usamos para levantar un servidor de desarrollo y construir el proyecto.

2. [Yarn][]: Lo usamos para manejar las dependencias de Node.

Estas herramientas nos permitirán trabajar de forma sencilla con los ficheros y las dependencias de la capa cliente de la aplicación (JavaScript). 

El siguiente paso es instalar las dependencias que se necesitan para trabajar con JavaScript en el proyecto. Para ello debe ejecutarse el comando:

```bash
yarn install
```

Generalmente este comando sólo es necesario ejecutarlo cuando modifiquemos las dependencias especificadas en nuestro proyecto (fichero [package.json](package.json)). 

Realizados los pasos anteriores, podemos proceder a arrancar la aplicación en "modo desarrollo".  Para ello necesitamos:

 1. Arrancar la parte servidora de la aplicación. Esto lo podemos hacer ejecutando la clase [CoetlApp](src/main/java/com/arte/application/template/CoetlApp.java). Al ejecutar esta clase se estamos levantando un servidor Tomcat embebido. Este Tomcat embebido nos permite desarrollar de forma más rápida y eficiente. 

 2. Ejecutar el siguiente comando de `yarn`:

    ```bash
    yarn start
    ```

Este comando arranca la parte cliente de la aplicación. Al ejecutar `yarn start` se abrirá una ventana en el navegador con la aplicación. Por defecto, la aplicación estará disponible en la URL [http://localhost:9000](http://localhost:9000).

A continuación, podemos comenzar a modificar los ficheros que deseemos en la aplicación. 
Si modificamos algún fichero de la parte servidora de la aplicación (ficheros Java y ficheros de configuración), debemos reiniciar el servidor Tomcat embebido que hemos levantado. 
Si modificamos algún fichero de la parte cliente de la aplicación (ficheros TS, CSS, HTML...), la aplicación se recargará de forma automática y podremos ver dichos cambios aplicados de forma inmediata en el navegador. 

### Ejecución de tests
En la aplicación se han incluido una serie de tests que permiten probar todas las funcionalidades incluidas.

Hay tests solo en la parte servidora. Son tests de [JUnit][] y están ubicados en [src/test/java/](src/test/java/). Pueden ejecutarse mendiante [Maven][]:

```bash
mvn clean test
```

### Más información
La base de la aplicación ha sido generada usando JHipster 4.6.2. Puede consultarse más información en la [página oficial](http://www.jhipster.tech/documentation-archive/v4.6.2 "JHipster").


[Node.js]: https://nodejs.org/
[Yarn]: https://yarnpkg.org/
[Webpack]: https://webpack.github.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Maven]: https://maven.apache.org/
[JUnit]: http://junit.org
