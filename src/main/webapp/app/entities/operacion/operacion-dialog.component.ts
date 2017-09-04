import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Response } from '@angular/http';

import { Observable, Subscription } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Operacion } from './operacion.model';
import { OperacionPopupService } from './operacion-popup.service';
import { OperacionService } from './operacion.service';
import { UserService, RolService } from '../../shared/index';

@Component({
    selector: 'jhi-operacion-dialog',
    templateUrl: './operacion-dialog.component.html'
})
export class OperacionDialogComponent implements OnInit {

    operacion: Operacion = new Operacion();
    isSaving: boolean;
    roles: any[];

    private subscription: Subscription;

    constructor(
        // public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private operacionService: OperacionService,
        private eventManager: JhiEventManager,
        private userService: UserService,
        private rolService: RolService,
        private route: ActivatedRoute,
        private router: Router,
    ) {
    }

    ngOnInit() {
        this.isSaving = false;

        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });

        this.roles = [];
        this.rolService.roles().subscribe((roles) => {
            this.roles = roles;
        });
    }

    load(id) {
        if (id) {
            this.operacionService.find(id).subscribe((operacion) => this.operacion = operacion);
        } else {
            this.operacion = new Operacion();
        }
    }

    clear() {
        this.back();
    }

    save() {
        this.isSaving = true;
        if (this.operacion.id !== undefined) {
            this.subscribeToSaveResponse(
                this.operacionService.update(this.operacion));
        } else {
            this.subscribeToSaveResponse(
                this.operacionService.create(this.operacion));
        }
    }

    private back() {
        this.router.navigate(['operacion']);
    }

    private subscribeToSaveResponse(result: Observable<Operacion>) {
        result.subscribe((res: Operacion) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Operacion) {
        this.eventManager.broadcast({ name: 'operacionListModification', content: 'OK' });
        this.isSaving = false;
        this.back();
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-operacion-popup',
    template: ''
})
export class OperacionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private operacionPopupService: OperacionPopupService
    ) { }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.operacionPopupService
                    .open(OperacionDialogComponent as Component, params['id']);
            } else {
                this.operacionPopupService
                    .open(OperacionDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
