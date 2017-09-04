import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { OperacionComponent } from './operacion.component';
import { OperacionDialogComponent } from './operacion-dialog.component';
import { OperacionDeletePopupComponent } from './operacion-delete-dialog.component';

@Injectable()
export class OperacionResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) { }

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

export const operacionRoute: Routes = [
    {
        path: 'operacion',
        component: OperacionComponent,
        resolve: {
            'pagingParams': OperacionResolvePagingParams
        },
        data: {
            roles: ['ROLE_USER'],
            pageTitle: 'secretariaLibroApp.operacion.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'operacion/:id',
        component: OperacionDialogComponent,
        data: {
            roles: ['ROLE_USER'],
            pageTitle: 'secretariaLibroApp.operacion.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'operacion-new',
        component: OperacionDialogComponent,
        data: {
            roles: ['ROLE_USER'],
            pageTitle: 'secretariaLibroApp.operacion.home.title'
        },
        canActivate: [UserRouteAccessService],
    },
    {
        path: 'operacion/:id/edit',
        component: OperacionDialogComponent,
        data: {
            roles: ['ROLE_USER'],
            pageTitle: 'secretariaLibroApp.operacion.home.title'
        },
        canActivate: [UserRouteAccessService],
    },
];

export const operacionPopupRoute: Routes = [
    {
        path: 'operacion/:id/delete',
        component: OperacionDeletePopupComponent,
        data: {
            roles: ['ROLE_USER'],
            pageTitle: 'secretariaLibroApp.operacion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
