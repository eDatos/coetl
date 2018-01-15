import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { JhiPaginationUtil } from 'ng-jhipster';

import { OperacionComponent } from './operacion.component';
import { OperacionDialogComponent } from './operacion-dialog.component';
import { UserRouteAccessService } from '../../shared/auth/user-route-access-service';

@Injectable()
export class OperacionResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
        };
    }
}

// FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)

export const operacionRoute: Routes = [
    {
        path: 'operacion',
        component: OperacionComponent,
        resolve: {
            'pagingParams': OperacionResolvePagingParams
        },
        data: {
            operaciones: 'LEER:OPERACION',
            pageTitle: 'arteApplicationTemplateApp.operacion.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'operacion/:id',
        component: OperacionDialogComponent,
        data: {
            operaciones: 'LEER:OPERACION',
            pageTitle: 'arteApplicationTemplateApp.operacion.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
];
