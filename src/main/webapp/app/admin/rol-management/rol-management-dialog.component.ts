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

    load(id) {
        if (id) {
            this.rolService.find(id).subscribe((rol) => this.rol = rol);
        } else {
            this.rol = new Rol();
        }
    }

    clear() {
        const returnPath = ['rol-management'];
        if (this.rol.nombre) {
            returnPath.push(this.rol.nombre.toString());
        }
        this.router.navigate(returnPath);
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
