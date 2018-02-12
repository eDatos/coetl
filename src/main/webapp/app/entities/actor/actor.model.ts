import { Pelicula } from '../pelicula/pelicula.model';
import { BaseEntity } from './../../shared';

export class Actor implements BaseEntity {

    constructor(
        public id?: number,
        public nombre?: string,
        public apellido1?: string,
        public apellido2?: string,
        public peliculas?: Pelicula[],
    ) {
    }

    public normalizeName(): string {
        return ''.concat(this.apellido1).concat(this.apellido2 ? ' ' + this.apellido2 : '').concat(', ').concat(this.nombre);
    }
}
