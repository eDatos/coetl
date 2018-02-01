import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Subscription } from 'rxjs/Rx';

import { GenericModalService, ITEMS_PER_PAGE, PAGINATION_OPTIONS, Principal, ResponseWrapper } from '../../shared';
import { ActorDialogComponent } from './actor-dialog.component';
import { Actor } from './actor.model';
import { ActorService } from './actor.service';

@Component({
    selector: 'jhi-actor',
    templateUrl: './actor.component.html'
})
export class ActorComponent implements OnInit, OnDestroy {

    actores: Actor[];
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
        private genericModalService: GenericModalService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router
    ) {
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
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    public editActor(actor: Actor): void {
        this.genericModalService.open(<any>ActorDialogComponent, { actor });
    }
}
