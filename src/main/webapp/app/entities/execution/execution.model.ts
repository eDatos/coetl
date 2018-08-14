export enum Type {
    AUTO = 'AUTO',
    MANUAL = 'MANUAL'
}

export enum Result {
    SUCCESS = 'SUCCESS',
    FAILED = 'FAILED',
    RUNNING = 'RUNNING',
    WAITING = 'WAITING',
    DUPLICATED = 'DUPLICATED'
}

export class Execution {
    constructor(
        public id?: number,
        public planningDate?: Date,
        public type?: Type,
        public result?: Result,
        public notes?: string,
        public idEtl?: number
    ) {}
}
