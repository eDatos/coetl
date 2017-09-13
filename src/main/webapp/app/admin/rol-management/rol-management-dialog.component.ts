import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { RolModalService } from './rol-modal.service';
import { JhiLanguageHelper, User, UserService, RolService, Rol } from '../../shared';
import { Operacion, OperacionService } from '../../entities/operacion/index';
import { Subscription } from 'rxjs/Rx';

@Component({
    selector: 'jhi-rol-mgmt-dialog',
    templateUrl: './rol-management-dialog.component.html'
})
export class RolMgmtDialogComponent implements OnInit {

    rol: Rol;
    nombreOriginalRol = '';
    operaciones: Operacion[];
    isSaving: Boolean;

    private subscription: Subscription;

    constructor(
        private rolService: RolService,
        private eventManager: JhiEventManager,
        private operacionService: OperacionService,
        private route: ActivatedRoute,
        private router: Router,
    ) { }

    ngOnInit() {
        this.isSaving = false;
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['nombre']);
        });
        this.operaciones = [];
        this.operacionService.query().subscribe((operaciones) => {
            this.operaciones = operaciones.json;
        });
    }

    operacionItemTemplate(operacion: Operacion) {
        return `${operacion.accion} - ${operacion.sujeto}`;
    }

    load(id) {
        if (id) {
            this.rolService.find(id).subscribe((rol) => {
                this.rol = rol
                this.nombreOriginalRol = this.rol.nombre;
            });
        } else {
            this.rol = new Rol();
        }
    }

    clear() {
        const returnPath = ['rol-management'];
        if (this.nombreOriginalRol) {
            returnPath.push(this.nombreOriginalRol);
        }
        this.router.navigate(returnPath);
    }

    save() {
        this.isSaving = true;
        if (!!this.nombreOriginalRol) {
            this.rolService.update(this.rol).subscribe((response) => this.onSaveSuccess(response), () => this.onSaveError());
        } else {
            this.rolService.create(this.rol).subscribe((response) => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }

    isEditMode(): Boolean {
        const lastPath = this.route.snapshot.url[this.route.snapshot.url.length - 1].path;
        return lastPath === 'editar' || lastPath === 'rol-management-new';
    }

    private back() {
        this.router.navigate(['rol-management']);
    }

    private onSaveSuccess(result) {
        this.eventManager.broadcast({ name: 'rolListModification', content: 'OK' });
        this.isSaving = false;
        this.back();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
