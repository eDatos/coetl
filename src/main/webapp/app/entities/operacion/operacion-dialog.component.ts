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
    roles: any[];

    private subscription: Subscription;

    constructor(
        private alertService: JhiAlertService,
        private operacionService: OperacionService,
        private rolService: RolService,
        private route: ActivatedRoute,
        private router: Router,
    ) {
    }

    ngOnInit() {
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
        const returnPath = ['operacion'];
        if (this.operacion.id) {
            returnPath.push(this.operacion.id.toString());
        }
        this.router.navigate(returnPath);
    }

    private back() {
        this.router.navigate(['operacion']);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
