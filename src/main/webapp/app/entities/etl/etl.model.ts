import { BaseVersionedAndAuditingWithDeletionEntity } from '../../shared/model/base-versioned-auditing-with-deletion-entity';
import { ExternalItem } from '../external-item';

export enum PentahoType {
    TRANSFORMATION = 'TRANSFORMATION',
    JOB = 'JOB'
}

export enum HopType {
    WORKFLOW = 'WORKFLOW',
    PIPELINE = 'PIPELINE'
}

export const Type = { ...PentahoType, ...HopType };
export type Type = typeof Type;

export enum ExecutionPlatform {
    PENTAHO = 'PENTAHO',
    APACHE_HOP = 'APACHE_HOP'
}

export class EtlBase extends BaseVersionedAndAuditingWithDeletionEntity {
    constructor(
        public id?: number,
        public code?: string,
        public name?: string,
        public organizationInCharge?: string,
        public type?: Type,
        public executionPlanning?: string,
        public externalItem?: ExternalItem,
        public nextExecution?: Date,
        public lastExecution?: Date,
        public executionPlatform?: ExecutionPlatform
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
        public uriRepository?: string,
        public isAttachedFilesChanged?: boolean
    ) {
        super();
    }
}
