import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { PeliculaPopupService } from './pelicula-popup.service';
import { Pelicula } from './pelicula.model';
import { PeliculaService } from './pelicula.service';

@Component({
    selector: 'jhi-pelicula-delete-dialog',
    templateUrl: './pelicula-delete-dialog.component.html'
})
export class PeliculaDeleteDialogComponent {

    pelicula: Pelicula;

    constructor(
        private peliculaService: PeliculaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss(false);
    }

    confirmDelete(id: number) {
        this.peliculaService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'peliculaListModification',
                content: 'Deleted an pelicula'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-pelicula-delete-popup',
    template: ''
})
export class PeliculaDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private peliculaPopupService: PeliculaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.peliculaPopupService
                .open(PeliculaDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
