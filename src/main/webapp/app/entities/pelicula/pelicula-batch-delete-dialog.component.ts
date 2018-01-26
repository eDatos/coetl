import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { PeliculaService } from './pelicula.service';

@Component({
    selector: 'ac-pelicula-batch-delete-dialog',
    templateUrl: './pelicula-batch-delete-dialog.component.html'
})
export class PeliculaBatchDeleteDialogComponent {

    query: string;

    constructor(
        private peliculaService: PeliculaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss(false);
    }

    confirmDelete() {
        this.peliculaService.deleteSelection({
            query: this.query
        }).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'peliculaListModification',
                content: 'Deleted selected peliculas'
            });
            this.activeModal.dismiss(true);
        });
    }
}
