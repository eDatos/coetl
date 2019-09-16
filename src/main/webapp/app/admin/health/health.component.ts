import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription, Observable } from 'rxjs';

import { GenericModalService } from '../../shared';
import { HealthService } from './health.service';
import { HealthDialogComponent } from './health-dialog.component';
import { HealthEditDialogComponent } from './health-edit-dialog.component';
import { HealthDeleteDialogComponent } from './health-delete-dialog.component';

@Component({
    selector: 'ac-health',
    templateUrl: './health.component.html',
    styleUrls: ['./health.component.scss']
})
export class HealthComponent implements OnInit, OnDestroy {
    public static EVENT_NAME = 'healthCheckEvent';

    healthData: any;
    updatingHealth: boolean;

    private checkSubscription: Subscription;

    constructor(
        private modalService: NgbModal,
        private genericModalService: GenericModalService,
        private healthService: HealthService,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.refresh();
        this.registerChangesOnHealth();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.checkSubscription);
    }

    baseName(name: string) {
        return this.healthService.getBaseName(name);
    }

    getBadgeClass(statusState) {
        if (statusState === 'UP') {
            return 'badge-success';
        } else {
            return 'badge-danger';
        }
    }

    refresh() {
        this.updatingHealth = true;

        Observable.forkJoin(
            this.healthService.checkHealth(),
            this.healthService.checkCustomHealth()
        ).subscribe(
            (health) => {
                this.healthData = [
                    ...this.healthService.transformHealthData(health[0]),
                    ...this.healthService.transformHealthData(health[1])
                ];
                this.updatingHealth = false;
            },
            (error) => {
                console.log(error);
                if (error.status === 503) {
                    this.healthData = this.healthService.transformHealthData(error.json());
                    this.updatingHealth = false;
                }
            }
        );
    }

    showHealth(health: any) {
        const modalRef = this.modalService.open(HealthDialogComponent);
        modalRef.componentInstance.currentHealth = health;
        modalRef.result.then(
            (result) => {
                // Left blank intentionally, nothing to do here
            },
            (reason) => {
                // Left blank intentionally, nothing to do here
            }
        );
    }

    subSystemName(name: string) {
        return this.healthService.getSubSystemName(name);
    }

    editCustomHealth(health?: any) {
        const customHealth = !health
            ? { id: null, serviceName: null, endpoint: null }
            : {
                  id: health.details.id,
                  serviceName: health.name,
                  endpoint: health.details.endpoint
              };
        this.genericModalService.open(HealthEditDialogComponent as Component, {
            currentHealth: customHealth
        });
        event.stopPropagation();
    }

    deleteCustomHealth(health: any) {
        this.genericModalService.open(HealthDeleteDialogComponent as Component, {
            currentHealth: health
        });
        event.stopPropagation();
    }

    private registerChangesOnHealth() {
        this.checkSubscription = this.eventManager.subscribe(HealthComponent.EVENT_NAME, () =>
            this.refresh()
        );
    }
}
