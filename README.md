# secretaria_libro
This application was generated using JHipster 4.6.2, you can find documentation and help at [https://jhipster.github.io/documentation-archive/v4.6.2](https://jhipster.github.io/documentation-archive/v4.6.2).

## Desarrollo

Para poder llevar a cabo la construcción de este proyecto será necesario que primero instales las siguientes dependencias:

1. [Node.js][]: Lo usamos para levantar un servidor de desarrollo y contruir el proyecto.
   
2. [Yarn][]: Lo usamos para manejar las dependencias de Node.
   

Después de haber instalado Node, podrás ejecutar el siguiente comando para instalar las herramientas de desarrollo.
Este comando sólo necesitarás ejecutarlo en los casos en los que las dependencias cambien en [package.json](package.json).

    yarn install

Utilizamos scripts de yarn y [Webpack][] como nuestro sistema de compilación.


Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
    yarn start

[Yarn][] también lo usamos para manejar los CSS y las dependencias de JavaScript usadas en la aplicaicón. Las dependencias pueden actualizarse
especificando una versión más reciente en [package.json](package.json). Además, también se puede usar `yarn update` y `yarn install` para manejar las dependencias.
Recuerda que puedes usar `help` junto con cualquier comando para obtener información sobre cómo funciona. Ejemplo, `yarn help update`.

El comando `yarn run` listará todos los scripts que se usan para construir el proyecto.

## Compilación para un entorno de producción

Para optimizar la aplicación para un entorno de producción deberemos ejecutar:

    ./mvnw -Pprod clean package

Este comando concatenará y minificará los archivos CSS y Javascript. Además, modificará el archivo `index.html` para que referencie a los nuevos archivos que se acaban de crear.
Para asegurarnos de que todo ha ido correctamente podemos ejecutar:

    java -jar target/*.war

Luego podremos probar en el navegador la siguiente dirección [http://localhost:8080](http://localhost:8080).


## Testing

Para lanzar los tests de la aplicación usaremos:

    ./mvnw clean test

### Tests de la capa cliente

Los tests unitarios se ejecutan con [Karma][] y están escritos con  [Jasmine][]. Estos tests se ubican en [src/test/javascript/](src/test/javascript/) y se pueden ejecutar con:

    yarn test

