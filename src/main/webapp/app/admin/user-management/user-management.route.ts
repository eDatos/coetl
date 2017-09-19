import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { JhiPaginationUtil } from 'ng-jhipster';

import { UserMgmtComponent } from './user-management.component';
import { UserMgmtDialogComponent } from './user-management-dialog.component';
import { UserDeleteDialogComponent } from './user-management-delete-dialog.component';

import { Principal, UserRouteAccessService } from '../../shared';

@Injectable()
export class UserResolvePagingParams implements Resolve<any> {

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

export const userMgmtRoute: Routes = [
    {
        path: 'user-management',
        canActivate: [UserRouteAccessService],
        component: UserMgmtComponent,
        resolve: {
            'pagingParams': UserResolvePagingParams
        },
        data: {
            pageTitle: 'userManagement.home.title',
            operaciones: 'LEER:USUARIO'
        }
    },
    {
        path: 'user-management/:login',
        canActivate: [UserRouteAccessService],
        component: UserMgmtDialogComponent,
        data: {
            pageTitle: 'userManagement.home.title',
            operaciones: 'LEER:USUARIO'
        },
    },
    {
        path: 'user-management-new',
        canActivate: [UserRouteAccessService],
        component: UserMgmtDialogComponent,
        data: {
            pageTitle: 'userManagement.home.title',
            operaciones: 'CREAR:USUARIO'
        }
    },
    {
        path: 'user-management/:login/edit',
        canActivate: [UserRouteAccessService],
        component: UserMgmtDialogComponent,
        data: {
            operaciones: 'EDITAR:USUARIO',
            pageTitle: 'userManagement.home.title'
        }
    }
];

export const userDialogRoute: Routes = [
    {
        path: 'user-management/:login/delete',
        canActivate: [UserRouteAccessService],
        component: UserDeleteDialogComponent,
        outlet: 'popup',
        data: {
            operaciones: 'BORRAR:USUARIO',
        }
    },
    {
        path: 'user-management/:login/restore',
        canActivate: [UserRouteAccessService],
        component: UserDeleteDialogComponent,
        outlet: 'popup',
        data: {
            operaciones: 'EDITAR:USUARIO',
        }
    }
];
