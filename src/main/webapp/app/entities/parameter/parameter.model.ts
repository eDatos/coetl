export enum Type {
    AUTO = 'AUTO',
    MANUAL = 'MANUAL'
}

export class Parameter {
    constructor(
        public id?: number,
        public key?: string,
        public value?: string,
        public type?: Type,
        public etlId?: number
    ) {}
}
