# UPGRADE - Proceso de actualización entre versiones

*Para actualizar de una versión a otra es suficiente con actualizar el WAR a la última versión. El siguiente listado presenta aquellos cambios de versión en los que no es suficiente con actualizar y que requieren por parte del instalador tener más cosas en cuenta. Si el cambio de versión engloba varios cambios de versión del listado, estos han de ejecutarse en orden de más antiguo a más reciente.*

*De esta forma, si tuviéramos una instalación en una versión **A.B.C** y quisieramos actualizar a una versión posterior **X.Y.Z** para la cual existan versiones anteriores que incluyan cambios listados en este documento, se deberá realizar la actualización pasando por todas estas versiones antes de poder llegar a la versión deseada.*

*EJEMPLO: Queremos actualizar desde la versión 1.0.0 a la 3.0.0 y existe un cambio en la base de datos en la actualización de la versión 1.0.0 a la 2.0.0.*

*Se deberá realizar primero la actualización de la versión 1.0.0 a la 2.0.0 y luego desde la 2.0.0 a la 3.0.0*

## 1.2.2 a x.y.z
* Se ha integrado el acceso al common-metadata, por lo que es necesario añadirlo los parámetros de configuración *environment.edatos.configuration.db.[PARAM]* al application-env.yml (ver archivo *src\main\resources\config\application-env.yml*)
* Se ha de añadir los parámetros de acceso a la base de datos al common-metadata. Para ello, se ha de ejecutar el script *etc\changes-from-release\1.1.2\db\common-metadata\postgresql\20210830_coetl_common_metadata_conf.sql* sustituyendo los valores por los apropiados. Puede tomarse como referencia los establecidos en el archivo de configuración application-env.yml en los parámetros *spring.datasource*.
* Se ha de añadir parámetros al common-metadata. Para ello, se ha de ejecutar el script *etc\changes-from-release\1.1.2\db\common-metadata\postgresql\20210913_add_properties_to_common_metadata.sql* sustituyendo los valores por los apropiados. Puede tomarse como referencia los establecidos en el archivo de configuración application-env.yml, atendiendo a los parámetros *application.cas.*\* y *pentaho.*\*.
* Se ha de añadir parámetros relativos al git al common-metadata. Para ello, se ha de ejecutar el script *etc\changes-from-release\1.1.2\db\common-metadata\postgresql\20210920_add_git_properties.sql * sustituyendo los valores por los apropiados.
* Eliminar del archivo de configuración application-env.yml los parámetros *spring.datasource*.
* Eliminar del archivo de configuración application-env.yml los parámetros *application.cas.*\*, *application.ldap.*\*, *pentaho.*\* y *spring.mail*\*
* Actualizar el WAR

## 0.0.0 a 1.2.2
* El proceso de actualizaciones entre versiones para versiones anteriores a la 1.2.2 está definido en el archivo README.md