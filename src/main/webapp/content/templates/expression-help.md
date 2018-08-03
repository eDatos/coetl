> Este campo acepta como valor cualquier expresión válida de *cron* para Quartz.

#### **Formato**

Una expresión cron está compuesta de 6 o 7 campos separados por un espacio en blanco. Estos campos pueden contener cualquiera de los valores permitidos, junto con varias combinaciones de los caracteres especiales permitidos para ese campo. Los campos son los siguientes:
```
·--------------------- segundos (0-59) - carácteres especiales (, - * /)
|  .------------------ minuto (0-59) - carácteres especiales (, - * /)
|  |  .--------------- hora (0-23) - carácteres especiales (, - * /)
|  |  |  .------------ día del mes (1-31) - carácteres especiales (, - * / ? L W)
|  |  |  |  .--------- mes (1-12) o jan,feb,mar,apr,may,jun,jul... (meses en inglés) - carácteres especiales (, - * /)
|  |  |  |  |  .------ día de la semana (0-6) (domingo=0 ó 7) o sun,mon,tue,wed,thu,fri,sat (días en inglés) - carácteres especiales (, - * / L #)
|  |  |  |  |  |  ·--- año (opcional, 1970-2099) - carácteres especiales (, - * /)
|  |  |  |  |  |  |
*  *  *  *  *  *  *
```

#### **Carácteres especiales**
* **"\*"** - sirve para indicar todos los valores. Por ejemplo, *cada minuto*.
* **"?"**" - sirve para indicar ningún valor específico. Por ejemplo, *no me importa que día del mes en particular*.
* **"-"** - sirve para especificar un rango de valores. Por ejemplo, *de lunes a viernes* ó *desde las 12 hasta las 18 horas*.
* **","** - sirve para concatenar valores. Por ejemplo, *los días lunes, miércoles y viernes* ó *los minutos 5, 12, 16, 45*.
* **"/"** - sirve para especificar el incremento. Por ejemplo, *cada 3 meses* ó *cada 15 minutos*.
* **"L"** - sirve para especificar el último de todos los valores posibles, si además en el mismo campo va seguido de un valor significa el último de ese valor. Por ejemplo, *el último día del mes* ó *el último viernes del mes*.
* **"W"** - sirve para especificar que el valor es un día laboral, si además en el mismo campo va seguido de un valor significa el valor más cercano a ese día laboral. Por ejemplo, *el día lobral más cercano al 5 de cada mes*.
> **NOTA:** Los carácteres especiales **"L"** y **"W"** pueden combinarse para indicar *la última semana laboral del mes*.
* **"#"** - sirve para especificar la anotación ordinal: primero, segundo, tercero, etc. Por ejemplo, *el tercer viernes de cada mes*.

#### **Ejemplo de expresiones más usuadas**

|Expresión CRON|Frecuencia|
|--------------|----------|
|`0 0/5 * * * ?`|Cada 5 minutos.|
|`0 0 12 * * ?`|A las 12 del mediodía cada día.|
|`0 15 10 ? * *`|A las 10:15 cada día.|
|`0 15 10 ? * MON-FRI`|A las 10:15 cada lunes, martes, miércoles, jueves y viernes.|
|`0 15 10 15 * ?`|A las 10:15 el día 15 de cada mes.|
|`0 15 10 ? * 6L`|A las 10:15 el último viernes de cada mes.|
|`0 15 10 L * ?`|A las 10:15 el último día de cada mes.|
|`0 15 10 * * ? 2018`|A las 10:15 cada día durante el año 2018.|
|`0 0 8 1 1/3 ?`|A las 08:00 el primer día de cada 3 meses.|
|`0 * 14 * * ?`|Cada minuto empezando a las 14:00, finalizando a las 14:59, cada día.|
|`0 11 11 11 11 ?`|Cada 11 de Noviembre a las 11:11.|

##### *Enlaces de interés*

<ul>
    <li>Más información sobre expresiones *cron* (en inglés): <a href="http://www.quartz-scheduler.org/documentation/quartz-2.x/tutorials/crontrigger.html" target="_blank">Página oficial de Quartz</a></li>
    <li>Más ejemplos de expresiones *cron*: <a href="https://www.ibm.com/support/knowledgecenter/es/SS4GCC_6.1.0/com.ibm.urelease.doc/topics/r_cronexpressions.html" target="_blank">IBM Knowledge Center</a></li>
    <li>Asistente generador de *cron* (en inglés): <a href="http://www.cronmaker.com/" target="_blank">Cron Maker</a></li>
</ul>