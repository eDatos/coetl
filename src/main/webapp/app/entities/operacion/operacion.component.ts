import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { Operacion } from './operacion.model';
import { OperacionService } from './operacion.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { OperacionFilter } from './operacion-search/index';

@Component({
    selector: 'jhi-operacion',
    templateUrl: './operacion.component.html'
})
export class OperacionComponent implements OnInit, OnDestroy {

    currentAccount: any;
    operaciones: Operacion[] = [];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    searchSubscription: Subscription;
    filters: OperacionFilter;

    constructor(
        private operacionService: OperacionService,
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

    loadAll(filters?: OperacionFilter) {
        filters = filters || this.filters;
        this.operacionService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort(),
            query: filters ? filters.toQuery() : '',
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
        this.router.navigate(['/operacion'], {
            queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate(['/operacion', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        this.filters = new OperacionFilter;
        this.principal.identity().then((account) => {
            this.currentAccount = account;
            this.registerChangeInOperacions();
            this.activatedRoute.queryParams.subscribe((params) => {
                this.filters.fromQueryParams(params);
                this.loadAll(this.filters);
            });

        });
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
        this.searchSubscription.unsubscribe();
    }

    trackId(index: number, item: Operacion) {
        return item.id;
    }
    registerChangeInOperacions() {
        this.eventSubscriber = this.eventManager.subscribe('operacionListModification', (response) => this.loadAll());
        this.searchSubscription = this.eventManager.subscribe('operacionSearch', (response) => this.router.navigate(['operacion'], { queryParams: response.content }));
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
        this.operaciones = data;
    }
    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
