import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Subscription } from 'rxjs/Rx';

import { GenericModalService, ITEMS_PER_PAGE, PAGINATION_OPTIONS, Principal, ResponseWrapper } from '../../shared';
import { Pelicula } from '../pelicula/pelicula.model';
import { PeliculaService } from '../pelicula/pelicula.service';
import { ActorDialogComponent } from './actor-dialog.component';
import { Actor } from './actor.model';
import { ActorService } from './actor.service';

@Component({
    selector: 'jhi-actor',
    templateUrl: './actor.component.html'
})
export class ActorComponent implements OnInit, OnDestroy {

    pruebas: any[];
    actores: Actor[];
    actoresUnionPeliculas: ActorUPelicula[];
    peliculas: Pelicula[];
    currentAccount: any;
    eventSubscriber: Subscription;
    searchSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        private actorService: ActorService,
        private peliculaService: PeliculaService,
        private genericModalService: GenericModalService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router
    ) {
        this.pruebas = [{id: 1, nombre: 'probando1', valor: 5}, {id: 2, nombre: 'probando2', valor: 4},
                      {id: 4, nombre: 'probando1', valor: 3}, {id: 3, nombre: 'probando2', valor: 5}];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.activatedRoute.queryParams
            .map((params) => params.size)
            .filter((size) => !!size)
            .subscribe((size) => this.itemsPerPage = PAGINATION_OPTIONS.indexOf(Number(size)) > -1 ? size : this.itemsPerPage);
    }

    loadAll() {
        this.actorService.query({
            page: this.page - 1,
            size: PAGINATION_OPTIONS.indexOf(Number(this.itemsPerPage)) > -1 ? this.itemsPerPage : ITEMS_PER_PAGE,
            sort: this.sort(),
        }).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/actor'], {queryParams:
            {
                page: this.page,
                size: PAGINATION_OPTIONS.indexOf(Number(this.itemsPerPage)) > -1 ? this.itemsPerPage : ITEMS_PER_PAGE,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    changeItemsPerPage(itemsPerPage: number) {
        this.itemsPerPage = itemsPerPage;
        this.transition();
    }

    clear() {
        this.page = 0;
        this.router.navigate(['/actor', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInActors();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Actor) {
        return item.id;
    }

    registerChangeInActors() {
        this.eventSubscriber = this.eventManager.subscribe('actorListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.actores = data;
        this.getPeliculasByActores(this.actores);
    }

    private getPeliculasByActores(actores: Actor[]) {
        const query = this.actores.length ? [`ACTORES IN (${actores.map((actor) => actor.id).join(',')})`] : '';
        this.peliculaService.query({
            query
        }).subscribe(
            (res: ResponseWrapper) => {
                this.peliculas = res.json;
                this.setActoresUnionPeliculas();
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    // Se mockea la entidad `Pelicula` como una entidad débil, como ejemplo para la tabla anidada.
    private setActoresUnionPeliculas() {
        this.actoresUnionPeliculas = [];
        this.actores.forEach((actor) => {
            let hasPelicula = false;
            const copy = Object.assign(new Actor(), actor);
            this.peliculas.filter((pelicula) => pelicula.actores.findIndex((predicate) => predicate.id === actor.id) > -1).forEach((pelicula) => {
                hasPelicula = true;
                const aux = Object.assign(new ActorUPelicula(), { actorId: copy.id, nombre: copy.normalizeName(), peliculaId: pelicula.id, titulo: pelicula.titulo });
                this.actoresUnionPeliculas.push(aux);
            });
            if (!hasPelicula) {
                const aux = Object.assign(new ActorUPelicula(), { actorId: copy.id, nombre: copy.normalizeName(), peliculaId: undefined, titulo: undefined });
                this.actoresUnionPeliculas.push(aux);
            }
        });
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    public editActor(actorId: number): void {
        const actor = this.actores.find((predicate) => predicate.id === actorId);
        this.genericModalService.open(<any>ActorDialogComponent, { actor: Object.assign(new Actor(), actor) });
    }

    sortProperty(event) {
        this.predicate = event.field;
        this.reverse = event.order > 0;
        this.loadAll();
    }

    public navigate(event) {
        this.router.navigate(['/pelicula', event.data.peliculaId]);
    }
}

class ActorUPelicula {
    constructor(
        actorId?: number,
        nombre?: string,
        peliculaId?: number,
        titulo?: string
    ) {}
}
