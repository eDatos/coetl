import { BaseEntity } from './../../shared';

export class Actor implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
        public apellido1?: string,
        public apellido2?: string,
        public peliculas?: BaseEntity[],
    ) {
    }
}
