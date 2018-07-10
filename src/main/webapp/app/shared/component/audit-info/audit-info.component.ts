import { Component, Input, OnInit } from '@angular/core';
import { BaseAuditingEntity } from '../../model/base-auditing-entity';

@Component({
    selector: 'ac-audit-info',
    templateUrl: './audit-info.component.html'
})
export class AuditInfoComponent implements OnInit {

    @Input()
    entity: BaseAuditingEntity;

    constructor() { }

    ngOnInit() {
        if (!this.isAuditable()) {
            throw new Error('The entity is not an auditable instance!');
        }
    }

    public hasAudit(): Boolean {
        return this.entity && this.isAuditable() && this.entity.id;
    }

    private isAuditable(): Boolean {
        return this.entity instanceof BaseAuditingEntity;
    }
}
