import { BaseAuditingWithDeletionEntity } from './base-auditing-with-deletion-entity';

export abstract class BaseVersionedAndAuditingWithDeletionEntity extends BaseAuditingWithDeletionEntity {
    constructor(optLock?: number) {
        super();
    }
}
