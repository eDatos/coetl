import { BaseEntity } from './../../shared';

export class Operacion implements BaseEntity {
    constructor(
        public id?: number,
        public accion?: string,
        public sujeto?: string,
        public roles?: string[],
    ) {
    }

    public toString(): string {
        return this.accion + ' ' + this.sujeto;
    }
}
