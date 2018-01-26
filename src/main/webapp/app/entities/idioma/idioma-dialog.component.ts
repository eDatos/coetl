import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Idioma } from './idioma.model';
import { IdiomaPopupService } from './idioma-popup.service';
import { IdiomaService } from './idioma.service';
import { Pelicula, PeliculaService } from '../pelicula';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-idioma-dialog',
    templateUrl: './idioma-dialog.component.html'
})
export class IdiomaDialogComponent implements OnInit {

    idioma: Idioma;
    isSaving: boolean;

    peliculas: Pelicula[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private idiomaService: IdiomaService,
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
        if (this.idioma.id !== undefined) {
            this.subscribeToSaveResponse(
                this.idiomaService.update(this.idioma));
        } else {
            this.subscribeToSaveResponse(
                this.idiomaService.create(this.idioma));
        }
    }

    private subscribeToSaveResponse(result: Observable<Idioma>) {
        result.subscribe((res: Idioma) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Idioma) {
        this.eventManager.broadcast({ name: 'idiomaListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-idioma-popup',
    template: ''
})
export class IdiomaPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private idiomaPopupService: IdiomaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.idiomaPopupService
                    .open(IdiomaDialogComponent as Component, params['id']);
            } else {
                this.idiomaPopupService
                    .open(IdiomaDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
