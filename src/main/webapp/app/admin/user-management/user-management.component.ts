import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiAlertService, JhiEventManager, JhiPaginationUtil, JhiParseLinks } from 'ng-jhipster';
import { Subscription } from 'rxjs/Rx';

import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper, Rol, RolService, User, UserService } from '../../shared';
import { UserFilter } from './user-search/index';

@Component({
    selector: 'jhi-user-mgmt',
    templateUrl: './user-management.component.html'
})
export class UserMgmtComponent implements OnInit, OnDestroy {

    currentAccount: any;
    users: User[];
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
    searchSubscription: Subscription;
    userListModification: Subscription;
    userFilter: UserFilter;
    roles: Rol[] = [];

    constructor(
        private userService: UserService,
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
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
    }

    ngOnInit() {
        this.userFilter = new UserFilter();

        this.principal.identity().then((account) => {
            this.currentAccount = account;
            this.rolService.query().subscribe((roles) => this.roles = roles.json);
            this.registerChangeInUsers();
            this.activatedRoute.queryParams.subscribe((params) => {
                this.userFilter.fromQueryParams(params);
                this.loadAll(this.userFilter);
            });
        });
    }

    ngOnDestroy() {
        this.routeData.unsubscribe();
        this.searchSubscription.unsubscribe()
        this.userListModification.unsubscribe();
    }

    registerChangeInUsers() {
        this.userListModification = this.eventManager.subscribe('userListModification', (response) => this.loadAll());
        this.searchSubscription = this.eventManager.subscribe('userSearch', (response) => {
            const queryParams = this.activatedRoute.snapshot.queryParams;
            this.router.navigate([this.activatedRoute.snapshot.url], { queryParams: Object.assign({}, queryParams, response.content) })
        });
    }

    loadAll(userFilter?: UserFilter) {
        this.userService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort(),
            query: userFilter ? userFilter.toQuery() : '',
            includeDeleted: userFilter ? userFilter.includeDeleted : false,
        }).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
            );
    }

    trackIdentity(index, item: User) {
        return item.id;
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
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
        const queryParams = Object.assign({}, this.activatedRoute.snapshot.queryParams);
        queryParams['page'] = this.page
        queryParams['predicate'] = this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        this.router.navigate(['/user-management'], { queryParams });
        this.loadAll(this.userFilter);
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.users = data;
    }

    private onError(error) {
        this.alertService.error(error.error, error.message, null);
    }
}
