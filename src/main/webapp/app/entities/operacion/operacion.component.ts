import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiAlertService } from 'ng-jhipster';

import { Operacion } from './operacion.model';
import { OperacionService } from './operacion.service';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { OperacionFilter } from './operacion-search/index';
import { ResponseWrapper } from '../../shared';
import { Principal } from '../../shared/auth/principal.service';

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
    predicate: any;
    reverse: any;
    searchSubscription: Subscription;
    filters: OperacionFilter;
    isEmptyList = false;

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
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
    }

    loadAll(filters?: OperacionFilter) {
        filters = filters || this.filters;
        this.operacionService.query({
            sort: this.sort(),
            query: filters ? filters.toQuery() : '',
        }).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers)
        );
    }

    transition() {
        this.router.navigate(['/operacion'], {
            queryParams: {
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.router.navigate(['/operacion', {
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

    private setIsEmptyList() {
        this.isEmptyList = this.operaciones && this.operaciones.length === 0;
    }

    private onSuccess(data, headers) {
        this.operaciones = data;
        this.setIsEmptyList();
    }
}
