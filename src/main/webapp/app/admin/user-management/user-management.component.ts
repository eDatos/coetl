import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { Principal, ResponseWrapper, User, UserService, Rol } from '../../shared';
import { UserFilter } from './user-search';
import { PermissionService } from '../../shared';
import { TranslateService } from '../../../../../../node_modules/@ngx-translate/core';

@Component({
    selector: 'jhi-user-mgmt',
    templateUrl: './user-management.component.html'
})
export class UserMgmtComponent implements OnInit, OnDestroy {
    // Atributos para la paginación
    page: number;
    totalItems: number;
    itemsPerPage: number;

    currentAccount: any;
    users: User[];
    error: any;
    success: any;
    routeData: any;
    links: any;
    predicate: any;
    reverse: any;
    searchSubscription: Subscription;
    userListModification: Subscription;
    userFilter: UserFilter;

    constructor(
        private userService: UserService,
        public permissionService: PermissionService,
        private parseLinks: JhiParseLinks,
        private principal: Principal,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private translateService: TranslateService
    ) {
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
            this.itemsPerPage = data['pagingParams'].itemsPerPage;
        });
    }

    ngOnInit() {
        this.userFilter = new UserFilter();

        this.principal.identity().then((account) => {
            this.currentAccount = account;
            this.registerChangeInUsers();
            this.activatedRoute.queryParams.subscribe((params) => {
                this.userFilter.fromQueryParams(params);
                this.loadAll(this.userFilter);
            });
        });
    }

    ngOnDestroy() {
        this.routeData.unsubscribe();
        this.searchSubscription.unsubscribe();
        this.userListModification.unsubscribe();
    }

    registerChangeInUsers() {
        this.userListModification = this.eventManager.subscribe(
            'userListModification',
            (response) => this.loadAll()
        );
        this.searchSubscription = this.eventManager.subscribe('userSearch', (response) => {
            this.page = 1;
            const queryParams = Object.assign(
                {},
                this.activatedRoute.snapshot.queryParams,
                response.content,
                { page: this.page }
            );
            this.router.navigate([this.activatedRoute.snapshot.url], { queryParams });
        });
    }

    loadAll(userFilter?: UserFilter) {
        this.userService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                query: userFilter ? userFilter.toQuery() : '',
                includeDeleted: userFilter ? userFilter.includeDeleted : false
            })
            .subscribe((res: ResponseWrapper) => this.onSuccess(res.json, res.headers));
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

    transition() {
        const queryParams = Object.assign({}, this.activatedRoute.snapshot.queryParams);
        queryParams['page'] = this.page;
        queryParams['predicate'] = this.predicate + ',' + (this.reverse ? 'asc' : 'desc');
        queryParams['size'] = this.itemsPerPage;
        this.router.navigate(['/user-management'], { queryParams });
    }

    getRoleName(rol: Rol): string {
        return this.translateService.instant(`coetlApp.rol.roles.${rol}`);
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.users = data;
    }
}
