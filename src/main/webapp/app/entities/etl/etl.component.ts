import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Subscription } from 'rxjs';

import { Account, Principal, ResponseWrapper } from '../../shared';
import { Etl } from './etl.model';
import { EtlFilter } from './etl-search';
import { EtlService } from './etl.service';

@Component({
    selector: 'ac-etl',
    templateUrl: 'etl.component.html'
})
export class EtlComponent implements OnInit, OnDestroy {
    page: number;
    totalItems: number;
    itemsPerPage: number;

    currentAccount: Account;
    etls: Etl[];
    eventSubscriber: Subscription;
    searchSubsctiption: Subscription;
    routeDataSubscription: any;
    links: any;
    predicate: any;
    reverse: any;
    filters: EtlFilter;

    constructor(
        private eventManager: JhiEventManager,
        private etlService: EtlService,
        private principal: Principal,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private translateService: TranslateService
    ) {
        this.routeDataSubscription = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
            this.itemsPerPage = data['pagingParams'].itemsPerPage;
        });

        this.filters = new EtlFilter();
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });

        this.activatedRoute.queryParams.subscribe((params) => {
            this.filters.fromQueryParams(params).subscribe(() => this.loadAll());
        });

        this.registerChangeInEtls();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
        this.eventManager.destroy(this.searchSubsctiption);
    }

    loadAll() {
        this.etlService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                query: this.filters ? this.filters.toQuery() : '',
                includeDeleted: this.filters ? this.filters.includeDeleted : false
            })
            .subscribe((res: ResponseWrapper) => this.onSuccess(res.json, res.headers));
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    transition() {
        this.router.navigate(['/etl'], {
            queryParams: Object.assign({}, this.activatedRoute.snapshot.queryParams, {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            })
        });
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/etl',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
    }

    trackId(index: number, item: Etl) {
        return item.id;
    }

    getTypeName(etl: Etl): string {
        return this.translateService.instant(`coetlApp.etl.type.${etl.type}`);
    }

    getPlanningMessage(etl: Etl): string {
        const messageCode = etl.isPlanning()
            ? 'coetlApp.etl.planning.isPlanning'
            : 'coetlApp.etl.planning.isNotPlanning';
        return this.translateService.instant(messageCode);
    }

    private registerChangeInEtls() {
        this.eventSubscriber = this.eventManager.subscribe('etlListModification', (response) =>
            this.loadAll()
        );
        this.searchSubsctiption = this.eventManager.subscribe('etlSearch', () => {
            this.page = 1;
            const queryParams = Object.assign(
                {},
                this.filters.toUrl(this.activatedRoute.snapshot.queryParams),
                { page: this.page }
            );
            this.router.navigate(['etl'], { queryParams });
        });
    }

    private onSuccess(data: Etl[], headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.etls = data;
    }
}
