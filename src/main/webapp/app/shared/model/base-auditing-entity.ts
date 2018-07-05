export abstract class BaseAuditingEntity {
    constructor(
        public createdBy?: string,
        public createdDate?: Date,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Date,
        public deletedBy?: string,
        public deletionDate?: Date
    ) {}
}
