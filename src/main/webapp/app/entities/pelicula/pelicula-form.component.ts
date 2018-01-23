import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Autosize } from 'ng-autosize';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Observable } from 'rxjs/Rx';
import { setTimeout } from 'timers';

import { ResponseWrapper } from '../../shared/model/response-wrapper.model';
import { Actor } from '../actor/actor.model';
import { ActorService } from '../actor/actor.service';
import { Categoria } from '../categoria/categoria.model';
import { CategoriaService } from '../categoria/categoria.service';
import { IdiomaService } from '../idioma';
import { Idioma } from '../idioma/idioma.model';
import { Pelicula } from './pelicula.model';
import { PeliculaService } from './pelicula.service';

@Component({
    selector: 'jhi-pelicula-form',
    templateUrl: './pelicula-form.component.html'
})
export class PeliculaFormComponent implements OnInit, AfterViewInit {

    pelicula: Pelicula;
    isSaving: boolean;
    @ViewChild(Autosize)
    descripcionContainer: Autosize;

    actores: Actor[] = []
    categorias: Categoria[] = [];
    idiomas: Idioma[] = [];

    constructor(
        private alertService: JhiAlertService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private peliculaService: PeliculaService,
        private actorService: ActorService,
        private categoriaService: CategoriaService,
        private idiomaService: IdiomaService,
        private eventManager: JhiEventManager
    ) {
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
    }

    ngAfterViewInit() {
        setTimeout(() => this.descripcionContainer.adjust(), null);
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
        this.eventManager.broadcast({ name: 'peliculaListModification', content: 'OK'});
        this.isSaving = false;
        this.router.navigate(['/pelicula', result.id]);
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

    /* delete() {
        this.modalService.open(<any>PeliculaDeleteDialogComponent, { pelicula: this.pelicula });
    } */

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
}
