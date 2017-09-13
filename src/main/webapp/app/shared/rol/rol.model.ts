import { Operacion } from '../../entities/operacion/index';

export class Rol {
    public id: number;
    public nombre: string;
    public operaciones: Operacion[];

    constructor(
        id?: number,
        nombre?: string,
        operaciones?: Operacion[],
    ) {
        this.id = id;
        this.nombre = !!nombre ? nombre : null;
        this.operaciones = !!operaciones ? operaciones : null;
    }
}
