import { BaseEntity } from './base-entity';

export abstract class BaseAuditingEntity implements BaseEntity {
    constructor(
        public id?: any,
        public createdBy?: string,
        public createdDate?: Date,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Date,
        public deletedBy?: string,
        public deletionDate?: Date
    ) {}
}
