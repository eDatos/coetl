import { BaseEntity } from './../../shared';

export class Idioma implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
        public peliculaId?: number,
    ) {
    }
}
