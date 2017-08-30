import { Operacion } from '../../entities/operacion/index';

export class Rol {
    public nombre: string;
    public operaciones: Operacion[];

    constructor(
        nombre?: string,
        operaciones?: Operacion[],
    ) {
        this.nombre = !!nombre ? nombre : null;
        this.operaciones = !!operaciones ? operaciones : null;
    }
}
