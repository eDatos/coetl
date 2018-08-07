export enum Type {
    AUTO = 'AUTO',
    MANUAL = 'MANUAL'
}

export enum Result {
    SUCCESS = 'SUCCESS',
    FAILED = 'FAILED',
    RUNNING = 'RUNNING',
    WAITING = 'WAITING'
}

export class Execution {
    constructor(
        public id?: number,
        public datetime?: Date,
        public type?: Type,
        public result?: Result,
        public notes?: string,
        public idEtl?: number
    ) {}
}
