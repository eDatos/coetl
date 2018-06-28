import { Route, Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { AuditsComponent } from './audits.component';
import { UserRouteAccessService, Rol } from '../../shared/index';
import { Injectable } from '@angular/core';
import { JhiPaginationUtil } from 'ng-jhipster';

@Injectable()
export class AuditsResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'auditEventDate,desc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
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
        roles: [Rol.ADMIN]
    },
    canActivate: [UserRouteAccessService]
};
