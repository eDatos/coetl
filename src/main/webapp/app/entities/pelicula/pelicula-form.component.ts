import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Autosize } from 'ng-autosize';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Observable, Subscription } from 'rxjs/Rx';
import { setTimeout } from 'timers';

import { GenericModalService } from '../../shared';
import { HasTitlesContainer } from '../../shared/layouts/side-menu/side-menu.component';
import { ResponseWrapper } from '../../shared/model/response-wrapper.model';
import { Actor } from '../actor/actor.model';
import { ActorService } from '../actor/actor.service';
import { Categoria } from '../categoria/categoria.model';
import { CategoriaService } from '../categoria/categoria.service';
import { Documento } from '../documento/documento.model';
import { DocumentoService } from '../documento/documento.service';
import { IdiomaService } from '../idioma';
import { Idioma } from '../idioma/idioma.model';
import { PeliculaDeleteDialogComponent } from './pelicula-delete-dialog.component';
import { Pelicula } from './pelicula.model';
import { PeliculaService } from './pelicula.service';

@Component({
    selector: 'jhi-pelicula-form',
    templateUrl: './pelicula-form.component.html'
})
export class PeliculaFormComponent implements OnInit, AfterViewInit, OnDestroy, HasTitlesContainer {

    readonly EVENT_NAME: String = 'peliculaListModification';

    instance: PeliculaFormComponent;

    pelicula: Pelicula;
    isSaving: boolean;
    @ViewChild(Autosize)
    descripcionContainer: Autosize;

    actores: Actor[] = [];
    categorias: Categoria[] = [];
    idiomas: Idioma[] = [];

    updatesSubscription: Subscription;

    resourceUrl: string;

    @ViewChild('titlesContainer')
    titlesContaner: ElementRef;

    constructor(
        private alertService: JhiAlertService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private peliculaService: PeliculaService,
        private actorService: ActorService,
        private categoriaService: CategoriaService,
        private genericModalService: GenericModalService,
        private idiomaService: IdiomaService,
        private documentoService: DocumentoService,
        private eventManager: JhiEventManager
    ) {
        this.resourceUrl = documentoService.resourceUrl;
        this.instance = this;
    }

    ngOnInit() {
        this.isSaving = false;
        if (this.activatedRoute.snapshot.data['pelicula']) {
            this.pelicula = this.activatedRoute.snapshot.data['pelicula'];
        } else {
            this.pelicula = new Pelicula();
            this.pelicula.actores = [];
            this.pelicula.categorias = [];
        }

        this.searchCategorias();
        this.searchIdiomas();
        this.searchActores();

        this.registerChangesOnPelicula();
    }

    ngAfterViewInit() {
        setTimeout(() => this.descripcionContainer.adjust(), null);
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.updatesSubscription);
    }

    private registerChangesOnPelicula() {
        this.updatesSubscription = this.eventManager.subscribe(this.EVENT_NAME, (result) => {
            if (result.content !== 'saved') {
                this.load(result.content);
            }
        });
    }

    private load(res: Pelicula) {
        this.pelicula = Object.assign({}, res);
    }

    clear() {
        if (this.pelicula.id) {
            this.router.navigate(['/pelicula', this.pelicula.id]);
        } else {
            this.router.navigate(['/pelicula']);
        }
    }

    save() {
        this.isSaving = true;
        if (this.pelicula.id !== undefined) {
            this.subscribeToSaveResponse(
                this.peliculaService.update(this.pelicula));
        } else {
            this.subscribeToSaveResponse(
                this.peliculaService.create(this.pelicula));
        }
    }

    private subscribeToSaveResponse(result: Observable<Pelicula>) {
        result.subscribe((res: Pelicula) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Pelicula) {
        this.isSaving = false;
        this.eventManager.broadcast({ name: this.EVENT_NAME, content: 'saved'});
        this.router.navigate(['pelicula', result.id]);
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

    delete() {
        this.genericModalService.open(<any>PeliculaDeleteDialogComponent, { pelicula: this.pelicula })
            .result.subscribe((res) => res ? this.router.navigate(['pelicula']) : null);
    }

    isEditMode(): Boolean {
        const lastPath = this.activatedRoute.snapshot.url[this.activatedRoute.snapshot.url.length - 1].path;
        return lastPath === 'edit' || lastPath === 'pelicula-new';
    }

    searchCategorias() {
        this.categoriaService.query({
            page: 0,
            size: 20,
            sort: 'asc'
        }).subscribe((res: ResponseWrapper) => {
            this.categorias = res.json;
        }, (res: ResponseWrapper) => this.onError(res.json()));
    }

    public categoriaItemTemplate(categoria: any): string {
        return `${categoria.nombre}`;
    }

    searchIdiomas() {
        this.idiomaService.query({
            page: 0,
            size: 20,
            sort: 'asc'
        }).subscribe((res: ResponseWrapper) => {
            this.idiomas = res.json;
        }, (res: ResponseWrapper) => this.onError(res.json()));
    }

    public idiomaItemTemplate(idioma: any): string {
        return `${idioma.nombre}`;
    }

    public searchActores() {
        this.actorService.query({
            page: 0,
            size: 20,
            sort: 'asc'/* ,
            query: [`PELICULA NE ${this.pelicula.id}`] */
        }).subscribe(
            (res: ResponseWrapper) => this.actores = res.json,
            (res: ResponseWrapper) => this.onError(res.json));
    }

    public actorItemTemplate(actor): string {
        return this.normalizedActorName(actor);
    }

    public normalizedActorName(actor: Actor): string {
        return ''.concat(actor.apellido2 ? actor.apellido2.concat(' ') : '').concat(actor.apellido1).concat(', ').concat(actor.nombre);
    }

    public deleteDocumento(file: Documento): void {
        this.peliculaService
            .desasociarDocumento(this.pelicula.id).subscribe(
            (res: Pelicula) => this.onDocumentoSuccess(res),
            (res: Response) => this.onSaveError(res));
    }

    public onDocumentoUpload(event) {
        const response = JSON.parse(event.xhr.response);
        this.peliculaService
            .asociarDocumento(this.pelicula.id, response.id).subscribe(
            (res: Pelicula) => this.onDocumentoSuccess(res),
            (res: Response) => this.onSaveError(res));
    }

    private onDocumentoSuccess(result: Pelicula) {
        this.eventManager.broadcast({ name: this.EVENT_NAME, content: result});
    }

    public getTitlesContainer() {
        return this.titlesContaner;
    }
}
