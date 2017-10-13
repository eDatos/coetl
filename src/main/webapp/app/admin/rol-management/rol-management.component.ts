import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager, JhiPaginationUtil, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ITEMS_PER_PAGE, Principal, User, UserService, ResponseWrapper, RolService, Rol } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { Subscription } from 'rxjs/Rx';

@Component({
    selector: 'jhi-rol-mgmt',
    templateUrl: './rol-management.component.html'
})
export class RolMgmtComponent implements OnInit, OnDestroy {

    currentAccount: any;
    roles: Rol[];
    error: any;
    success: any;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    rolListModificationSubscription: Subscription;

    public firstQueryFinished: boolean;

    constructor(
        private rolService: RolService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private principal: Principal,
        private eventManager: JhiEventManager,
        private paginationUtil: JhiPaginationUtil,
        private paginationConfig: PaginationConfig,
        private activatedRoute: ActivatedRoute,
        private router: Router
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.firstQueryFinished = false;
    }

    public comprobarMostrarImg(): boolean {
        if (this.roles !== null && this.roles.length > 0 && this.comprobarMostrarImg) {
            return true;
        }
        return false;
    }
    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
            this.loadAll();
            this.registerChangeInUsers();
        });
    }

    ngOnDestroy() {
        this.routeData.unsubscribe();
        this.rolListModificationSubscription.unsubscribe();
    }

    registerChangeInUsers() {
        this.rolListModificationSubscription = this.eventManager.subscribe('rolListModification', (response) => this.loadAll());
    }

    loadAll() {
        this.rolService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort(),
        }).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
            );
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'nombre') {
            result.push('nombre');
        }
        return result;
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/rol-management'], {
            queryParams: {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    private onSuccess(data, headers) {
        this.roles = data;
        this.firstQueryFinished = true;
    }

    private onError(error) {
        this.alertService.error(error.error, error.message, null);
    }
}
