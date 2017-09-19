import { Operacion } from '../../entities/operacion/index';

export class Rol {

    public id: number;
    public codigo: string;
    public nombre: string;
    public operaciones: Operacion[];

    constructor(
        id?: number,
        codigo?: string,
        nombre?: string,
        operaciones?: Operacion[],
    ) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = !!nombre ? nombre : null;
        this.operaciones = !!operaciones ? operaciones.map((o) => new Operacion(o.id, o.accion.toString(), o.sujeto.toString())) : null;
    }

}
