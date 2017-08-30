import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Rol, RolService } from '../../shared';
import { RolModalService } from './rol-modal.service';

@Component({
    selector: 'jhi-rol-mgmt-delete-dialog',
    templateUrl: './rol-management-delete-dialog.component.html'
})
export class RolMgmtDeleteDialogComponent {

    rol: Rol;

    constructor(
        private rolService: RolService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(nombre) {
        this.rolService.delete(nombre).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'rolListModification',
                content: 'Deleted a rol'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-rol-delete-dialog',
    template: ''
})
export class RolDeleteDialogComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rolModalService: RolModalService
    ) { }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.rolModalService.open(RolMgmtDeleteDialogComponent as Component, params['nombre']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
