import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ActorDeletePopupComponent } from './actor-delete-dialog.component';
import { ActorPopupComponent } from './actor-dialog.component';
import { ActorComponent } from './actor.component';

@Injectable()
export class ActorResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
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
            authorities: ['ROLE_USER'],
            pageTitle: 'arteApplicationTemplateApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const actorPopupRoute: Routes = [
    {
        path: 'actor-new',
        component: ActorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'arteApplicationTemplateApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'actor/:id/edit',
        component: ActorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'arteApplicationTemplateApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'actor/:id/delete',
        component: ActorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'arteApplicationTemplateApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
