import { Route, Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuditsComponent } from './audits.component';
import { UserRouteAccessService, ITEMS_PER_PAGE } from '../../shared';
import { Injectable } from '@angular/core';
import { JhiPaginationUtil } from 'ng-jhipster';
import { HERRAMIENTAS_ROLES } from '../../shared';
import { BASE_DECIMAL } from '../../app.constants';

@Injectable()
export class AuditsResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'auditEventDate,desc';
        const itemsPerPage = route.queryParams['size'] ? route.queryParams['size'] : ITEMS_PER_PAGE;
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort),
            itemsPerPage: parseInt(itemsPerPage, BASE_DECIMAL)
        };
    }
}

export const auditsRoute: Route = {
    path: 'audits',
    component: AuditsComponent,
    resolve: {
        'pagingParams': AuditsResolvePagingParams
    },
    data: {
        pageTitle: 'audits.title',
        roles: HERRAMIENTAS_ROLES
    },
    canActivate: [UserRouteAccessService]
};
