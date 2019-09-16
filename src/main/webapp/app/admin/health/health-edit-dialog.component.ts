import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { HealthService } from './health.service';
import { HealthComponent } from './health.component';

@Component({
    selector: 'ac-health-edit-dialog',
    templateUrl: 'health-edit-dialog.component.html'
})
export class HealthEditDialogComponent {
    public currentHealth: any;

    constructor(
        private activeModal: NgbActiveModal,
        private healthService: HealthService,
        private eventManager: JhiEventManager
    ) {}

    close() {
        this.activeModal.dismiss('closed');
    }

    save() {
        const editObservable = this.currentHealth.id
            ? this.healthService.updateCustomHealth(this.currentHealth)
            : this.healthService.createCustomHealth(this.currentHealth);

        editObservable.subscribe((response) => this.onSaveSuccess(), (error) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.eventManager.broadcast({ name: HealthComponent.EVENT_NAME, content: 'saved' });
        this.activeModal.close(true);
    }

    private onSaveError() {
        this.activeModal.close(false);
    }
}
