import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Operacion } from './operacion.model';
import { OperacionPopupService } from './operacion-popup.service';
import { OperacionService } from './operacion.service';

@Component({
    selector: 'jhi-operacion-delete-dialog',
    templateUrl: './operacion-delete-dialog.component.html'
})
export class OperacionDeleteDialogComponent {

    operacion: Operacion;

    constructor(
        private operacionService: OperacionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.operacionService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'operacionListModification',
                content: 'Deleted an operacion'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-operacion-delete-popup',
    template: ''
})
export class OperacionDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private operacionPopupService: OperacionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.operacionPopupService
                .open(OperacionDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
