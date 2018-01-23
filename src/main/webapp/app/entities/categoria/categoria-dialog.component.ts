import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Categoria } from './categoria.model';
import { CategoriaPopupService } from './categoria-popup.service';
import { CategoriaService } from './categoria.service';
import { Pelicula, PeliculaService } from '../pelicula';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-categoria-dialog',
    templateUrl: './categoria-dialog.component.html'
})
export class CategoriaDialogComponent implements OnInit {

    categoria: Categoria;
    isSaving: boolean;

    peliculas: Pelicula[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private categoriaService: CategoriaService,
        private peliculaService: PeliculaService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.peliculaService.query()
            .subscribe((res: ResponseWrapper) => { this.peliculas = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.categoria.id !== undefined) {
            this.subscribeToSaveResponse(
                this.categoriaService.update(this.categoria));
        } else {
            this.subscribeToSaveResponse(
                this.categoriaService.create(this.categoria));
        }
    }

    private subscribeToSaveResponse(result: Observable<Categoria>) {
        result.subscribe((res: Categoria) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Categoria) {
        this.eventManager.broadcast({ name: 'categoriaListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
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

    trackPeliculaById(index: number, item: Pelicula) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-categoria-popup',
    template: ''
})
export class CategoriaPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private categoriaPopupService: CategoriaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.categoriaPopupService
                    .open(CategoriaDialogComponent as Component, params['id']);
            } else {
                this.categoriaPopupService
                    .open(CategoriaDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
