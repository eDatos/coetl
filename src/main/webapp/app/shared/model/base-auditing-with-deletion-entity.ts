import { BaseAuditingEntity } from './base-auditing-entity';

export abstract class BaseAuditingWithDeletionEntity extends BaseAuditingEntity {
    constructor(public deletedBy?: string, public deletionDate?: Date) {
        super();
    }
}
