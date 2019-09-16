import { BaseAuditingEntity } from './base-auditing-entity';

export abstract class BaseVersionedAndAuditingEntity extends BaseAuditingEntity {
    constructor(optLock?: number) {
        super();
    }
}
