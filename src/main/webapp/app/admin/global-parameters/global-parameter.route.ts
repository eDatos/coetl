import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes, Route } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService, ITEMS_PER_PAGE, HERRAMIENTAS_ROLES } from '../../shared';
import { GlobalParameterComponent } from './global-parameter.component';

@Injectable()
export class ParameterResolvePagingParams implements Resolve<any> {
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

export const parameterRoute: Route = {
    path: 'global-parameters',
    component: GlobalParameterComponent,
    resolve: {
        pagingParams: ParameterResolvePagingParams
    },
    data: {
        roles: HERRAMIENTAS_ROLES,
        pageTitle: 'coetlApp.etl.home.title'
    },
    canActivate: [UserRouteAccessService]
};
