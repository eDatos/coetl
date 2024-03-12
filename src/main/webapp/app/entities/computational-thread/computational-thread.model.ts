import { BaseVersionedAndAuditingWithDeletionEntity } from '../../shared/model/base-versioned-auditing-with-deletion-entity';

export class ComputationalThreadsBase extends BaseVersionedAndAuditingWithDeletionEntity {
    constructor(
        public id?: number,
        public code?: string,
        public name?: string,
        public organizationInCharge?: string,
        public executionPlanning?: string,
        public nextExecution?: Date
    ) {
        super();
    }

    isDeleted(): boolean {
        return !!this.deletionDate;
    }

    isPlanning(): boolean {
        return !!this.executionPlanning;
    }
}

export class ComputationalThreads extends ComputationalThreadsBase {
    constructor(
        public purpose?: string,
        public functionalInCharge?: string,
        public technicalInCharge?: string,
        public comments?: string,
        public executionDescription?: string
    ) {
        super();
    }
}
