import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService, ITEMS_PER_PAGE } from '../../shared';
import { ActorPopupComponent } from './actor-dialog.component';
import { ActorComponent } from './actor.component';
import { ACTOR_ROLES } from '../../shared';
import { BASE_DECIMAL } from '../../app.constants';

@Injectable()
export class ActorResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        const itemsPerPage = route.queryParams['size'] ? route.queryParams['size'] : ITEMS_PER_PAGE;
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort),
            itemsPerPage: parseInt(itemsPerPage, BASE_DECIMAL)
      };
    }
}

export const actorRoute: Routes = [
    {
        path: 'actor',
        component: ActorComponent,
        resolve: {
            'pagingParams': ActorResolvePagingParams,
        },
        data: {
            roles: ACTOR_ROLES,
            pageTitle: 'coetlApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const actorPopupRoute: Routes = [
    {
        path: 'actor-new',
        component: ActorPopupComponent,
        data: {
            roles: ACTOR_ROLES,
            pageTitle: 'coetlApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'actor/:id/edit',
        component: ActorPopupComponent,
        data: {
            roles: ACTOR_ROLES,
            pageTitle: 'coetlApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
