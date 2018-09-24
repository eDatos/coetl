import { BaseVersionedAndAuditingWithDeletionEntity } from '../../shared/model/base-versioned-auditing-with-deletion-entity';
import { File } from '../file/file.model';

export enum Type {
    TRANSFORMATION = 'TRANSFORMATION',
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
        public executionPlanning?: string,
        public nextExecution?: Date,
        public etlFile?: File,
        public etlDescriptionFile?: File
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
