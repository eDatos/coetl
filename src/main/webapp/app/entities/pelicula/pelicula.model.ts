import { Actor } from '../actor/actor.model';
import { Categoria } from '../categoria/categoria.model';
import { Idioma } from '../idioma/idioma.model';
import { BaseEntity } from './../../shared';

export class Pelicula implements BaseEntity {
    constructor(
        public id?: number,
        public titulo?: string,
        public descripcion?: string,
        public annoEstreno?: any,
        public idioma?: Idioma,
        public actores?: Actor[],
        public categorias?: Categoria[],
    ) {
    }
}
