import { DatePipe } from '@angular/common';
import { Component, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { Pelicula } from './pelicula.model';
import { PeliculaService } from './pelicula.service';

@Injectable()
export class PeliculaPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private peliculaService: PeliculaService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.peliculaService.find(id).subscribe((pelicula) => {
                    pelicula.fechaEstreno = this.datePipe
                        .transform(pelicula.fechaEstreno, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.peliculaModalRef(component, pelicula);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.peliculaModalRef(component, new Pelicula());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    peliculaModalRef(component: Component, pelicula: Pelicula): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.pelicula = pelicula;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
