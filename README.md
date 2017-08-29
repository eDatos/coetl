# secretaria_libro
En este tutorial se explica el proceso a seguir para llevar a cabo el proceso de instalación de la aplicación. Se recomienda llevar a cabo una
lectura rápida de todo el tutorial antes de comenzar con el proceso de instalación.
Además, podrá encontrar otros anexos de utilidad en los que se detallan algunas cuestiones relacionadas con el desarrollo, los diferentes perfiles de compilación,...

----------

## Introducción

### Descripción de la aplicación
El libro de Resoluciones y Decretos de Secretaría es la solución corporativa del ECIT para llevar a cabo el manejo de los documentos de tipo resolución y decreto. 
La aplicación está basada en tecnologías web estándar (HTML5, CSS3, Javascript y Java). 
La implantación de la solución permite llevar a cabo las siguientes tareas: 
- Manejar los usuarios y roles con acceso a la aplicación. 
- Gestionar el ciclo de vida de los documentos de tipo resolución y decreto.
- Gestionar los firmantes de los documentos.
- Gestionar los grupos de firmantes. 
- Gestionar las ausencias planificadas de cada uno de los firmantes.
Además, la aplicación cuenta con un servicio web de tipo REST que permite <TODO>.

### Requerimientos previos

#### Requisitos del entorno
En este apartado se especifican los requisitos necesarios, referidos al entorno, para que la aplicación funcione adecuadamente:
- Apache Tomcat.  8.5
- Java. 1.8.x
- PostgreSQL.  TODO 


#### Dependencias
La aplicación requiere de determinados servicios para poder estar completamente operativa. Algunos de ellos son necesarios de manera directa y otros de manera indirecta:
Los servicios necesarios de manera directa (son atacados directamente por la aplicación):
- Kafka. Se utiliza para interactuar con los servicios de notificación de cambios en portafirmas y en la base de datos de organización. 
- Servicio de Gestión Documental. Se utiliza para albergar los documentos una vez que han sido firmados. 
- CAS. Se utiliza para llevar a cabo las labores de autenticación.
Los servicios necesarios de manera indirecta (son atacados por servicios que, a su vez, requiere la aplicación):
- Servicio de Notificación de cambios en Base de Datos de Organización (BDO). Se utiliza para manejar la relación de áreas y servicios.
- Servicio de Notificación de cambios en Portafirmas. Se utiliza para enviar los documentos a firmar por el firmante del área y ser notificado de que el cambio se ha realizado correctamente. 
- LDAP. Se utiliza para validar las credenciales de los usuarios. 


### Componentes de la instalación
La aplicación consta de una única aplicación que estará disponible para el uso interno del personal del ECIT.
La aplicación consta de una interfaz web y de un servicio web REST.

----------

## Procedimiento de instalación desde cero

### Paso 1. Configuración del entorno
- Configuración del servidor de aplicaciones según los requisitos del entorno ya especificados.
- Configuración de la base de datos. 
    1. La aplicación cuenta con un mecanismo para llevar a cabo la gestión de los cambios de base de datos de manera automatizada (liquidbase).
    2. A priori lo único que es necesario es crear en la aplicación el esquema de base de datos que se va a utilizar. El esquema debe recibir el nombre de "TODO".
    2. Por otra parte, será necesario que el usuario que use la aplicación tenga permisos para la creación de objetos sobre el esquema anterior.
    3. De esta forma la aplicación, en el momento de arrancar, llevará a cabo la creación de todos los objetos que sean necesarios sobre la base de datos.

### Paso 2. Despliegue en servidor de aplicaciones.
- Ubicar el archivo *.war compilado con el perfil adecuado en el servidor de aplicaciones. Se pueden obtener más detalles en el [Anexo de perfiles de compilación](http://git.arte-consultores.com/ecit/secretaria-libro/edit/master/README.md#anexo-perfiles-de-compilaci%C3%B3n). 
- Definir si se desea externalizar la configuración de la aplicación o mantener dentro del propio archivo war.
    1. Si no se desea externalizar la configuración de las propiedades a un directorio DATA se puede omitir este paso.
    2. Si se desea externalizar la configuración de las propiedades a un directorio DATA se tendrán que realizar los siguientes pasos:
        - Se hará una copia del archivo **secretaria-libro.war/WEB-INF/classes/config/application-env.yml** a un directorio externalizado de su elección.
        - Se editará el archivo **secretaria-libro.war/WEB-INF/classes/config/data-location.properties** y se especificará la ruta anterior.
- Se cumplimentarán las propiedades del fichero **application-env.yml**. Se puede consultar el detalle sobre cada una de las propiedades en el [Anexo de descripción de propiedades de configuración](http://git.arte-consultores.com/ecit/secretaria-libro/blob/master/README.md#anexo-descripci%C3%B3n-de-las-propiedades-de-configuraci%C3%B3n).


### Paso 3. Configuración de logging.
Pueden modificarse los ficheros relacionados con la configuración de logging en **secretaria-libro.war/WEB-INF/classes/logback.xml**.


### Paso 4. Arrancar la aplicación.

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
- `application.cas.endpoint`
   - Endpoint donde se localiza el CAS.
- `application.cas.applicationHome`
   - Endpoint de la aplicación, a donde volverá del CAS para terminar la autenticación.
- `application.cas.login`
   - URL a la que se debe acceder para realizar la acción de login. Sólo debe cumplimentarse en el caso se que su valor sea distinto a `application.cas.endopoint`+ '/login'.
- `application.cas.logout`
   - URL a la que se debe acceder para realizar la acción de logout. Sólo debe cumplimentarse en el caso se que su valor sea distinto a `application.cas.endopoint`+ '/logout'.
- `application.cas.validate`
   - URL a la que se debe acceder para realizar la acción de logout. Sólo debe cumplimentarse en el caso se que su valor sea distinto a `application.cas.endopoint`+ '/logout'.
-  `debug`
   - Permite aumentar el nivel de log a DEBUG. 


## Anexo. Perfiles de compilación
Existen dos perfiles de compilación que deberemos usar según el entorno: `dev` para entorno de desarrollo y `env` para entornos de producción.

En la aplicación existirá un archivo general `application.yml` en el que se ubicarán todas las propiedades de configuración comunes a todos los perfiles y que rara vez se editan.

Para configurar cada entorno, también existirán archivos específicos con las propiedades que pueden ser modificadas por el usuario y dependientes del entorno, teniendo así un fichero `application-env.yml` para configurar las propiedades del entorno de producción, y un `application-dev.yml` para el entorno de desarrollo.

Cuando compilemos el proyecto, se usará una configuración u otra en función del perfil indicado durante la compilación. Hay que tener en cuenta que, en el caso de que una misma propiedad exista tanto en el fichero general `application.yml` como en el dependiente del entorno, tendrá preferencia el valor configurado en el fichero de configuración del entorno.
Para compilar el proyecto con el perfil de producción es necesario ejecutar lo siguiente:

    mvn clean install -Penv

Por otro lado, para compilar el proyecto con el perfil de desarrollo es necesario ejecutar:

    mvn clean install -Pdev

Hay que tener presente que si la aplicación se compila con el perfil de desarrollo, para terminar de construir la misma y poder ejecutarla es necesario seguir los pasos descritos en el [Anexo de desarrollo](http://git.arte-consultores.com/ecit/secretaria-libro#anexo-desarrollo).

## Anexo. Desarrollo

Para poder llevar a cabo la construcción de este proyecto será necesario que primero instales las siguientes dependencias:

1. [Node.js][]: Lo usamos para levantar un servidor de desarrollo y contruir el proyecto.
   
2. [Yarn][]: Lo usamos para manejar las dependencias de Node.
   

Después de haber instalado Node, podrás ejecutar el siguiente comando para instalar las herramientas de desarrollo.
Este comando sólo necesitarás ejecutarlo en los casos en los que las dependencias cambien en [package.json](package.json).

    yarn install

Utilizamos scripts de yarn y [Webpack][] como nuestro sistema de compilación.

Puedes simplificar el proceso de desarrollo lanzando los siguientes comandos en distintas terminales que levantarán un servidor local mediante springboot (mvnw) y un proxy para gestionar la compilación y construcción del javascript mediante webpack (yarn start). Cuando modifiques los archivos, se actualizará automaticamente el servidor con tus cambios.

    ./mvnw
    yarn start

[Yarn][] también lo usamos para manejar los CSS y las dependencias de JavaScript usadas en la aplicaicón. Las dependencias pueden actualizarse
especificando una versión más reciente en [package.json](package.json). Además, también se puede usar `yarn update` y `yarn install` para manejar las dependencias.
Recuerda que puedes usar `help` junto con cualquier comando para obtener información sobre cómo funciona. Ejemplo, `yarn help update`.

El comando `yarn run` listará todos los scripts que se usan para construir el proyecto.


## Anexo. Testing

Para lanzar los tests de la aplicación usaremos:

    ./mvnw clean test

### Tests de la capa cliente

Los tests unitarios se ejecutan con [Karma][] y están escritos con  [Jasmine][]. Estos tests se ubican en [src/test/javascript/](src/test/javascript/) y se pueden ejecutar con:

    yarn test


[Node.js]: https://nodejs.org/
[Yarn]: https://yarnpkg.org/
[Webpack]: https://webpack.github.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html