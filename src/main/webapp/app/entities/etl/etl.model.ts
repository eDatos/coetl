import { BaseVersionedAndAuditingWithDeletionEntity } from '../../shared/model/base-versioned-auditing-with-deletion-entity';

export enum Type {
    ETL = 'ETL',
    JOB = 'JOB'
}

export class Etl extends BaseVersionedAndAuditingWithDeletionEntity {
    constructor(
        public id?: number,
        public code?: string,
        public name?: string,
        public purpose?: string,
        public organizationInCharge?: string,
        public functionalInCharge?: string,
        public technicalInCharge?: string,
        public type?: Type,
        public comments?: string,
        public executionDescription?: string,
        public executionPlanning?: string
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
