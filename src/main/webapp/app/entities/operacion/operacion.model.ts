import { BaseEntity } from './../../shared';

export class Operacion implements BaseEntity {
    constructor(
        public id?: number,
        public accion?: string,
        public sujeto?: string,
        public roles?: string[],
    ) {
    }
}
