import { Pelicula } from '../pelicula/pelicula.model';
import { Documento } from '../documento/documento.model';
import { BaseEntity } from '../../shared';

export enum Genero {
    MASCULINO = 'MASCULINO',
    FEMENINO = 'FEMENINO'
}

export class Actor implements BaseEntity {

    constructor(
        public id?: number,
        public nombre?: string,
        public apellido1?: string,
        public apellido2?: string,
        public genero?: Genero,
        public oscarizado = false,
        public peliculas?: Pelicula[],
        public documentos?: Documento[]
    ) {
    }

    public normalizeName(): string {
        return ''.concat(this.apellido1).concat(this.apellido2 ? ' ' + this.apellido2 : '').concat(', ').concat(this.nombre);
    }
}
