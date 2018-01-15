import { BaseEntity } from './../../shared';
import { TipoAccionOperacion } from './tipo-accion-operacion'
import { TipoSujetoOperacion } from './tipo-sujeto-operacion';

export class Operacion implements BaseEntity {

    public accion: TipoAccionOperacion;
    public sujeto?: TipoSujetoOperacion;

    constructor(
        public id?: number,
        accion?: string,
        sujeto?: string,
    ) {
        this.id = id ? id : null;
        if (!!accion) {
            this.accion = TipoAccionOperacion[accion];
        }
        if (!!sujeto) {
            this.sujeto = TipoSujetoOperacion[sujeto];
        }
    }

    public toString(): string {
        return this.accion + ' ' + this.sujeto;
    }
}
