import { Component, Input, OnInit } from '@angular/core';
import { BaseAuditingEntity } from '../../model/base-auditing-entity';

@Component({
    selector: 'ac-audit-info',
    templateUrl: './audit-info.component.html'
})
export class AuditInfoComponent implements OnInit {

    @Input()
    entity: any;

    constructor() { }

    ngOnInit() {
        if (!this.isAuditable()) {
            console.warn('The entity is not an auditable instance!', this.entity);
        };
    }

    public hasAudit(): Boolean {
        return this.entity && this.entity.id && this.isAuditable();
    }

    private isAuditable(): Boolean {
        return this.entity instanceof BaseAuditingEntity;
    }
}
