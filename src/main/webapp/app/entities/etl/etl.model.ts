import { BaseVersionedAndAuditingWithDeletionEntity } from '../../shared/model/base-versioned-auditing-with-deletion-entity';
import { File } from '../file/file.model';

export enum Type {
    TRANSFORMATION = 'TRANSFORMATION',
    JOB = 'JOB'
}

export class EtlBase extends BaseVersionedAndAuditingWithDeletionEntity {
    constructor(
        public id?: number,
        public code?: string,
        public name?: string,
        public organizationInCharge?: string,
        public type?: Type,
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

export class Etl extends EtlBase {
    constructor(
        public purpose?: string,
        public functionalInCharge?: string,
        public technicalInCharge?: string,
        public comments?: string,
        public executionDescription?: string,
        public nextExecution?: Date,
        public etlFile?: File,
        public etlDescriptionFile?: File,
        public attachedFiles?: File[],
        public isAttachedFilesChanged?: boolean
    ) {
        super();
        this.isAttachedFilesChanged = !!isAttachedFilesChanged;
    }
}
