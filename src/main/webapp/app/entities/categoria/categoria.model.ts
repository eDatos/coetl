import { BaseEntity } from '../../shared';

export class Categoria implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
        public peliculas?: BaseEntity[],
    ) {
    }
}
