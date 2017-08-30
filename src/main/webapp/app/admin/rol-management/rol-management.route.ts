import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { JhiPaginationUtil } from 'ng-jhipster';

import { RolMgmtComponent } from './rol-management.component';
import { RolMgmtDetailComponent } from './rol-management-detail.component';
// import { UserDialogComponent } from './user-management-dialog.component';
// import { UserDeleteDialogComponent } from './user-management-delete-dialog.component';

import { Principal } from '../../shared';
import { RolDialogComponent } from './rol-management-dialog.component';

@Injectable()
export class RolResolve implements CanActivate {

    constructor(private principal: Principal) { }

    canActivate() {
        return this.principal.identity().then((account) => this.principal.hasAnyRol(['ROLE_ADMIN']));
    }
}

@Injectable()
export class RolResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'nombre,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
        };
    }
}

export const rolMgmtRoute: Routes = [
    {
        path: 'rol-management',
        component: RolMgmtComponent,
        resolve: {
            'pagingParams': RolResolvePagingParams
        },
        data: {
            pageTitle: 'rolManagement.home.title'
        }
    },
    {
        path: 'rol-management/:nombre',
        component: RolMgmtDetailComponent,
        data: {
            pageTitle: 'rolManagement.home.title'
        }
    }
];

export const rolDialogRoute: Routes = [
    {
        path: 'rol-management-nuevo',
        component: RolDialogComponent,
        outlet: 'popup'
    },
    {
        path: 'rol-management/:nombre/editar',
        component: RolDialogComponent,
        outlet: 'popup'
    },
    //     {
    //         path: 'user-management/:login/delete',
    //         component: UserDeleteDialogComponent,
    //         outlet: 'popup'
    //     }
];
