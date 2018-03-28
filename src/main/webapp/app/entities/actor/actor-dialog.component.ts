import { Component, OnDestroy, OnInit } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Observable } from 'rxjs/Rx';

import { ResponseWrapper } from '../../shared';
import { Pelicula, PeliculaFilter, PeliculaService } from '../pelicula';
import { ActorPopupService } from './actor-popup.service';
import { Actor, Genero } from './actor.model';
import { ActorService } from './actor.service';
import { Documento } from '../documento/documento.model';
import { DocumentoService } from '../documento/documento.service';

@Component({
    selector: 'jhi-actor-dialog',
    templateUrl: './actor-dialog.component.html',
    styleUrls: ['./actor-dialog.component.scss']
})
export class ActorDialogComponent implements OnInit {

    actor: Actor;
    isSaving: boolean;
    isDeleting: boolean;
    resourceUrl: string;

    peliculas: Pelicula[];
    copiaPeliculas: Pelicula[];
    peliculaFilter: PeliculaFilter;
    generos: any[];
    selectedGenero: any;
    ordenandoPeliculas = false;
    canAddPeliculas = true;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private actorService: ActorService,
        private transalteService: TranslateService,
        private peliculaService: PeliculaService,
        private documentoService: DocumentoService,
        private eventManager: JhiEventManager,
        private router: Router
    ) {
        this.resourceUrl = documentoService.resourceUrl;
        this.peliculaFilter = new PeliculaFilter();
    }

    ngOnInit() {
        this.isSaving = false;
        this.isDeleting = false;
        if (this.actor.id !== undefined) {
            this.peliculaFilter.actores = Array.of<Actor>(this.actor);
            this.peliculaService.query({ query: this.peliculaFilter.toQuery() })
                .subscribe((res: ResponseWrapper) => { this.peliculas = res.json; });
        }
        this.generos = Object.keys(Genero).map((key) => Object.assign({}, {
            id: key, value: this.transalteService.instant('arteApplicationTemplateApp.actor.genero.' + key)
        }));
        this.selectedGenero = this.generos.find((g) => g.id === this.actor.genero);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        this.actor.genero = this.selectedGenero && this.selectedGenero.id ? this.selectedGenero.id : undefined;
        if (this.actor.id !== undefined) {
            this.subscribeToSaveResponse(
                this.actorService.update(this.actor));
        } else {
            this.subscribeToSaveResponse(
                this.actorService.create(this.actor));
        }
    }

    private subscribeToSaveResponse(result: Observable<Actor>) {
        result.subscribe((res: Actor) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Actor) {
        this.eventManager.broadcast({ name: 'actorListModification', content: 'Actor saved'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
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

    public toggleIsDeleting() {
        this.isDeleting = !this.isDeleting;
    }

    public confirmDelete(id: number) {
        this.actorService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({name: 'actorListModification', content: 'Actor deleted'});
            this.activeModal.dismiss(true);
        },
        (error) => this.onDeleteError(error));
    }

    private onDeleteError(error): void {
        this.isDeleting = false;
    }

    public generoItemTemplate(item: any) {
        return item.value;
    }

    public crearNuevaPelicula() {
        this.clear();
        this.actorService.actorSource.next(this.actor);
        this.router.navigate(['pelicula-new']);
    }

    public activarReordenarPeliculas() {
        this.ordenandoPeliculas = true;
        this.copiaPeliculas = this.peliculas.map((pelicula) => Object.assign({}, pelicula));
    }

    public cancelarReordenarPeliculas() {
        this.peliculas = this.copiaPeliculas.map((pelicula) => Object.assign({}, pelicula));
        this.ordenandoPeliculas = false;
    }

    public guardarReordenarPeliculas() {
        console.log('This is a mock! It is needed to prepare backend!');
    }

    public onDocumentoUpload(event) {
        const nuevoDocumento = JSON.parse(event.xhr.response);
        this.subscribeToDocumentModification(this.actorService.asociarDocumento(this.actor.id, nuevoDocumento.id));
    }

    public deleteDocumento(documento: Documento): void {
        this.subscribeToDocumentModification(this.actorService.desasociarDocumento(this.actor.id, documento.id));
    }

    private subscribeToDocumentModification(result: Observable<Actor>) {
        result.subscribe((res: Actor) => {
            this.actor = res;
            this.eventManager.broadcast({name: 'actorListModification', content: 'Actor deleted'});
        })
    }
}

@Component({
    selector: 'jhi-actor-popup',
    template: ''
})
export class ActorPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private actorPopupService: ActorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.actorPopupService
                    .open(ActorDialogComponent as Component, params['id']);
            } else {
                this.actorPopupService
                    .open(ActorDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
