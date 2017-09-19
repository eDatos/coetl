import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { Rol, RolService } from '../../shared';

@Injectable()
export class RolModalService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private rolService: RolService
    ) { }

    open(component: Component, codigo?: string): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (codigo) {
            this.rolService.find(codigo).subscribe((rol) => this.rolModalRef(component, rol));
        } else {
            // Este setTimeout es un fix para evitar el error "ExpressionChangedAfterItHasBeenCheckedError" que aparece.
            // Ver: https://github.com/jhipster/generator-jhipster/issues/5985
            setTimeout(() => { this.rolModalRef(component, new Rol()) }, 0);
        }
    }

    rolModalRef(component: Component, rol: Rol): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.rol = rol;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
