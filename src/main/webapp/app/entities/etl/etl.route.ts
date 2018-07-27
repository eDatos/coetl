import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService, READ_ETL_ROLES, ITEMS_PER_PAGE } from '../../shared';
import { EtlComponent } from './etl.component';

@Injectable()
export class EtlResolvePagingParams implements Resolve<any> {
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

export const etlRoute: Routes = [
    {
        path: 'etl',
        component: EtlComponent,
        resolve: {
            pagingParams: EtlResolvePagingParams
        },
        data: {
            roles: READ_ETL_ROLES,
            pageTitle: 'coetlApp.etl.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
