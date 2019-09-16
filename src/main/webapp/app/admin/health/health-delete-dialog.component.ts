import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { HealthService } from './health.service';
import { HealthComponent } from './health.component';

@Component({
    selector: 'ac-health-delete-dialog',
    templateUrl: 'health-delete-dialog.component.html'
})
export class HealthDeleteDialogComponent {
    public currentHealth: any;

    constructor(
        private activeModal: NgbActiveModal,
        private healthService: HealthService,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('closed');
    }

    delete() {
        const heatlhId = this.currentHealth.details.id;
        this.healthService
            .deleteCustomHealth(heatlhId)
            .subscribe((response) => this.onSaveSuccess(), (error) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.eventManager.broadcast({ name: HealthComponent.EVENT_NAME, content: 'deleted' });
        this.activeModal.close(true);
    }

    private onSaveError() {
        this.activeModal.close(false);
    }
}
