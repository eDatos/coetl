import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Categoria } from './categoria.model';
import { CategoriaPopupService } from './categoria-popup.service';
import { CategoriaService } from './categoria.service';

@Component({
    selector: 'jhi-categoria-delete-dialog',
    templateUrl: './categoria-delete-dialog.component.html'
})
export class CategoriaDeleteDialogComponent {

    categoria: Categoria;

    constructor(
        private categoriaService: CategoriaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.categoriaService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'categoriaListModification',
                content: 'Deleted an categoria'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-categoria-delete-popup',
    template: ''
})
export class CategoriaDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private categoriaPopupService: CategoriaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.categoriaPopupService
                .open(CategoriaDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
