import { Operacion } from '../../entities/operacion/index';

export class Rol {
    public id: number;
    public name: string;
    public operaciones: Operacion[];

    constructor(
        id?: number,
        name?: string,
        operaciones?: Operacion[],
    ) {
        this.id = !!id ? id : null;
        this.name = !!name ? name : null;
        this.operaciones = !!operaciones ? operaciones : null;
    }
}
