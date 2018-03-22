import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { JhiPaginationUtil } from 'ng-jhipster';

import { UserMgmtComponent } from './user-management.component';
import { UserMgmtFormComponent } from './user-management-form.component';
import { UserDeleteDialogComponent } from './user-management-delete-dialog.component';

import { Principal, UserRouteAccessService } from '../../shared';
import { DEFAULT_PATH, DEFAULT_OPERACIONES } from '../../home/home.component';

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
        path: DEFAULT_PATH,
        canActivate: [UserRouteAccessService],
        component: UserMgmtComponent,
        resolve: {
            'pagingParams': UserResolvePagingParams
        },
        data: {
            pageTitle: 'userManagement.home.title',
            operaciones: DEFAULT_OPERACIONES
        }
    },
    {
        path: 'user-management/:login',
        canActivate: [UserRouteAccessService],
        component: UserMgmtFormComponent,
        data: {
            pageTitle: 'userManagement.home.title',
            operaciones: 'LEER:USUARIO'
        },
    },
    {
        path: 'user-management-new',
        canActivate: [UserRouteAccessService],
        component: UserMgmtFormComponent,
        data: {
            pageTitle: 'userManagement.home.title',
            operaciones: 'CREAR:USUARIO'
        }
    },
    {
        path: 'user-management/:login/edit',
        canActivate: [UserRouteAccessService],
        component: UserMgmtFormComponent,
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
            operaciones: 'DESACTIVAR:USUARIO',
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
