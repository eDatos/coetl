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
        path: '',
        canActivate: [UserRouteAccessService],
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
            },
            {
                path: 'rol-management/:nombre',
                component: RolMgmtDialogComponent,
                data: {
                    pageTitle: 'rolManagement.home.title',
                    operaciones: ['LEER:ROL']
                }
            },
            {
                path: 'rol-management-new',
                component: RolMgmtDialogComponent,
                data: {
                    operaciones: ['CREAR:ROL'],
                }
            },
            {
                path: 'rol-management/:nombre/editar',
                component: RolMgmtDialogComponent,
                data: {
                    operaciones: ['EDITAR:ROL'],
                },
            },
        ]
    }
];

export const rolDialogRoute: Routes = [
    {
        path: 'rol-management/:nombre/borrar',
        component: RolDeleteDialogComponent,
        outlet: 'popup',
        data: {
            operaciones: [{ accion: TipoAccionOperacion.LEER, sujeto: TipoAccionOperacion.BORRAR }],
        },
        canActivate: [UserRouteAccessService],
    }
];
