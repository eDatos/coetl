import { ActivatedRouteSnapshot, Resolve, Routes } from '@angular/router';

import {
    READ_ETL_ROLES,
    MANAGE_ETL_ROLES,
    UserRouteAccessService,
    ITEMS_PER_PAGE
} from '../../shared';
import { ComputationalThreadComponent } from './computational-thread.component';
import { ComputationalThreadFormComponent } from './form/computational-thread-form.component';
import { ComputationalThreadResolve } from './computational-thread-resolve.service';
import { Injectable } from '@angular/core';
import { JhiPaginationUtil } from 'ng-jhipster';

@Injectable()
export class ComputationalThreadResolvePagingParams implements Resolve<any> {
    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        const itemsPerPage = route.queryParams['itemsPerPage']
            ? route.queryParams['itemsPerPage']
            : ITEMS_PER_PAGE;
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort),
            itemsPerPage: Number(itemsPerPage)
        };
    }
}

export const computationalThreadRoute: Routes = [
    {
        path: 'computational-threads',
        component: ComputationalThreadComponent,
        resolve: {
            pagingParams: ComputationalThreadResolvePagingParams
        },
        data: {
            pageTitle: 'computationalThread.pageTitle',
            roles: READ_ETL_ROLES
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'computational-threads-new',
        component: ComputationalThreadFormComponent,
        data: {
            roles: MANAGE_ETL_ROLES,
            pageTitle: 'computationalThread.pageTitle'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'computational-threads/:idThread',
        component: ComputationalThreadFormComponent,
        resolve: {
            computationalThread: ComputationalThreadResolve
        },
        data: {
            roles: READ_ETL_ROLES,
            pageTitle: 'computationalThread.pageTitle'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'computational-threads/:idThread/edit',
        component: ComputationalThreadFormComponent,
        resolve: {
            computationalThread: ComputationalThreadResolve
        },
        data: {
            roles: MANAGE_ETL_ROLES,
            pageTitle: 'computationalThread.pageTitle'
        },
        canActivate: [UserRouteAccessService]
    }
];
