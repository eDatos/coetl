import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { JhiPaginationUtil } from 'ng-jhipster';

import { RolMgmtComponent } from './rol-management.component';
import { RolMgmtDialogComponent } from './rol-management-dialog.component';
import { RolDeleteDialogComponent } from './rol-management-delete-dialog.component';

import { Principal, UserRouteAccessService } from '../../shared';
import { TipoAccionOperacion, TipoSujetoOperacion } from '../../entities/operacion/index';

@Injectable()
export class RolResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'codigo,asc';
        return {
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
        };
    }
}

export const rolMgmtRoute: Routes = [
    {
        path: '',
        children: [
            {
                path: 'rol-management',
                component: RolMgmtComponent,
                resolve: {
                    'pagingParams': RolResolvePagingParams
                },
                data: {
                    pageTitle: 'rolManagement.home.title',
                    operaciones: ['LEER:ROL'],
                },
                canActivate: [UserRouteAccessService],
            },
            {
                path: 'rol-management/:codigo',
                component: RolMgmtDialogComponent,
                data: {
                    pageTitle: 'rolManagement.home.title',
                    operaciones: ['LEER:ROL']
                },
                canActivate: [UserRouteAccessService],
            },
            {
                path: 'rol-management-new',
                component: RolMgmtDialogComponent,
                data: {
                    pageTitle: 'rolManagement.home.title',
                    operaciones: ['CREAR:ROL'],
                },
                canActivate: [UserRouteAccessService],
            },
            {
                path: 'rol-management/:codigo/editar',
                component: RolMgmtDialogComponent,
                data: {
                    pageTitle: 'rolManagement.home.title',
                    operaciones: ['EDITAR:ROL'],
                },
                canActivate: [UserRouteAccessService],
            },
        ]
    }
];

export const rolDialogRoute: Routes = [
    {
        path: 'rol-management/:codigo/borrar',
        component: RolDeleteDialogComponent,
        outlet: 'popup',
        data: {
            operaciones: ['ELIMINAR:ROL'],
        },
        canActivate: [UserRouteAccessService],
    }
];
