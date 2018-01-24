import { DatePipe } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiAlertService, JhiEventManager, JhiPaginationUtil, JhiParseLinks } from 'ng-jhipster';
import { Subscription } from 'rxjs/Rx';

import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { CategoriaService } from '../categoria/categoria.service';
import { IdiomaService } from '../idioma/idioma.service';
import { PeliculaFilter } from './pelicula-search/pelicula-filter.model';
import { Pelicula } from './pelicula.model';
import { PeliculaService } from './pelicula.service';

@Component({
    selector: 'jhi-pelicula',
    templateUrl: './pelicula.component.html'
})
export class PeliculaComponent implements OnInit, OnDestroy {

    currentAccount: any;
    peliculas: Pelicula[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    searchSubsctiption: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    filters: PeliculaFilter;

    constructor(
        private datePipe: DatePipe,
        private peliculaService: PeliculaService,
        private categoriaService: CategoriaService,
        private idiomaService: IdiomaService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private paginationUtil: JhiPaginationUtil,
        private paginationConfig: PaginationConfig
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
    }

    toggleVisibleSelection() {
        return this.filters.batchSelection.toggleIds(this.peliculas.map((pelicula) => pelicula.id));
    }

    allVisibleIdsAreSelected() {
        if (!this.peliculas) { return false; }
        return this.filters.batchSelection.allVisibleIdsAreSelected(this.peliculas.map((pelicula) => pelicula.id));
    }

    loadAll() {
        this.peliculaService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort(),
            query: this.filters.toQuery()
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
        this.router.navigate(['/pelicula'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
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
        this.router.navigate(['/pelicula', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

    ngOnInit() {
        this.filters = new PeliculaFilter(this.datePipe);
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.categoriaService.query().subscribe((res) => {
            this.filters.allCategorias = res.json;
            this.activatedRoute.queryParams.subscribe((params) => {
                this.filters.fromQueryParams(params);
                this.loadAll();
            });
        });
        this.idiomaService.query().subscribe((res) => {
            this.filters.allIdiomas = res.json;
            this.activatedRoute.queryParams.subscribe((params) => {
                this.filters.fromQueryParams(params);
                this.loadAll();
            });
        });

        this.registerChangeInPeliculas();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
        this.eventManager.destroy(this.searchSubsctiption);
    }

    trackId(index: number, item: Pelicula) {
        return item.id;
    }

    registerChangeInPeliculas() {
        this.eventSubscriber = this.eventManager.subscribe('peliculaListModification', (response) => this.loadAll());
        this.searchSubsctiption = this.eventManager.subscribe('peliculaSearch', (response) => {
            const queryParams = Object.assign({}, this.filters.toUrl(this.activatedRoute.snapshot.queryParams));
            this.router.navigate(['pelicula'], {queryParams})
        });
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
        // this.page = pagingParams.page;
        this.peliculas = data;
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
