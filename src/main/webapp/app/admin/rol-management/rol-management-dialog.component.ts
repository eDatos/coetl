import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { RolModalService } from './rol-modal.service';
import { User, UserService, RolService, Rol } from '../../shared';
import { Operacion, OperacionService, OperacionFilter } from '../../entities/operacion';
import { Subscription } from 'rxjs/Rx';

@Component({
    selector: 'jhi-rol-mgmt-dialog',
    templateUrl: './rol-management-dialog.component.html',
    styleUrls: ['./rol-management-dialog.component.scss']
})
export class RolMgmtDialogComponent implements OnInit {

    rol: Rol;
    codigoOriginalRol = '';
    operaciones: Operacion[] = [];
    isSaving: Boolean;
    operacionesString: any[];

    private subscription: Subscription;
    private operacionFilter: OperacionFilter;

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
            this.load(params['codigo']);
        });
        this.operaciones = [];
        this.operacionFilter = new OperacionFilter();
        this.loadOperaciones();
    }

    loadOperaciones() {
        const criterias = [];
        if (this.operacionFilter && this.operacionFilter.toOrQuery()) {
            criterias.push('(' + this.operacionFilter.toOrQuery() + ')');
        }
        if (this.excludeSelectedOperacionesQuery()) {
            criterias.push('(' + this.excludeSelectedOperacionesQuery() + ')');
        }
        this.operacionService.query().subscribe((operaciones) => {
            this.operaciones = operaciones.json;
            this.operacionesString = this.operaciones.map((operacion) => {
                return {
                    label: operacion.accion + ' - ' + operacion.sujeto,
                    value: operacion
                }
            });
        });
    }

    excludeSelectedOperacionesQuery() {
        let operacionesIds = '';
        if (this.rol) {
            operacionesIds = this.rol.operaciones
                .map((operacion) => `(id NE ${operacion.id})`)
                .join(' AND ');
        }
        return operacionesIds; // ? `ID NOT IN (${operacionesIds})` : '';
    }

    filterOperaciones($event) {
        this.operacionFilter.accion = $event.query;
        this.operacionFilter.sujeto = $event.query;
        this.loadOperaciones();
    }

    load(id) {
        if (id) {
            this.rolService.find(id).subscribe((rol) => {
                this.rol = rol
                this.codigoOriginalRol = this.rol.codigo;
            });
        } else {
            this.rol = new Rol();
            this.rol.operaciones = [];
        }
    }

    clear() {
        const returnPath = ['rol-management'];
        if (this.codigoOriginalRol) {
            returnPath.push(this.codigoOriginalRol);
        }
        this.router.navigate(returnPath);
    }

    save() {
        this.isSaving = true;
        if (!!this.codigoOriginalRol) {
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
