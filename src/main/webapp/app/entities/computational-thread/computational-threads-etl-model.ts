import { Etl } from '../etl/etl.model';

export class ComputationalThreadsEtl {
    constructor(public id?: number, public etl?: Etl, public executionOrder?: number) {}
}
