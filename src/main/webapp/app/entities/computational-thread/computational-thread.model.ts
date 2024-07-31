import { BaseVersionedAndAuditingWithDeletionEntity } from '../../shared/model/base-versioned-auditing-with-deletion-entity';
import { ExternalItem } from '../external-item';
import { ComputationalThreadsEtl } from './computational-threads-etl-model';

export class ComputationalThreadsBase extends BaseVersionedAndAuditingWithDeletionEntity {
    constructor(
        public id?: number,
        public code?: string,
        public name?: string,
        public executionDescription?: string,
        public executionPlanning?: string,
        public nextExecution?: Date,
        public lastExecution?: Date,
        public externalItem?: ExternalItem,
        public computationalThreadsEtl?: ComputationalThreadsEtl[]
    ) {
        super();
    }

    public isDeleted(): boolean {
        return !!this.deletionDate;
    }

    public isPlanning(): boolean {
        return !!this.executionPlanning;
    }
}

export class ComputationalThreads extends ComputationalThreadsBase {
    constructor(public description?: string) {
        super();
    }
}
