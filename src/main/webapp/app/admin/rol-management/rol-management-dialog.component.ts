import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { RolModalService } from './rol-modal.service';
import { JhiLanguageHelper, User, UserService, RolService, Rol } from '../../shared';
import { Operacion, OperacionService } from '../../entities/operacion/index';

@Component({
    selector: 'jhi-rol-mgmt-dialog',
    templateUrl: './rol-management-dialog.component.html'
})
export class RolMgmtDialogComponent implements OnInit {

    rol: Rol;
    operaciones: Operacion[];
    isSaving: Boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private rolService: RolService,
        private eventManager: JhiEventManager,
        private operacionService: OperacionService,
    ) { }

    ngOnInit() {
        this.isSaving = false;
        this.operaciones = [];
        this.operacionService.query().subscribe((operaciones) => {
            this.operaciones = operaciones.json;
        });
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.rol.nombre !== null) {
            this.rolService.update(this.rol).subscribe((response) => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }

    compareOperations(r1: Operacion, r2: Operacion): boolean {
        return r1 && r2 ? r1.id === r2.id : r1 === r2;
    }

    private onSaveSuccess(result) {
        this.eventManager.broadcast({ name: 'rolListModification', content: 'OK' });
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-rol-dialog',
    template: ''
})
export class RolDialogComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rolModalService: RolModalService
    ) { }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['nombre']) {
                this.modalRef = this.rolModalService.open(RolMgmtDialogComponent as Component, params['nombre']);
            } else {
                this.modalRef = this.rolModalService.open(RolMgmtDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
