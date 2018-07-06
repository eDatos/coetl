import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';

/**
 * Date pipe compatible con navegadores IE11 y Edge.
 * @description Solución realizada con Moment.js para evitar la incompatibilidad de navegadores Microsoft.
 * Los formatos válidos son los que acepte el método format de Moment.js
 * @example template: {{exampleDate | acDate:'DD/MM/YYYY HH:mm'}}
 * @see issues: https://github.com/angular/angular/issues/9524
 * @link solution: https://stackoverflow.com/questions/39728481/angular2-date-pipe-does-not-work-in-ie-11-and-edge-13-14
 * @link Moment.js formats options: https://momentjs.com/docs/#/displaying/format/
 */
@Pipe({
    name: 'acDate'
})
export class AcDatePipe implements PipeTransform {
    transform(value: any, format = ''): string {
        const momentDate = moment(value);
        return momentDate.isValid() ? momentDate.format(format) : value;
    }
}
