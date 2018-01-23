import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CategoriaComponent } from './categoria.component';
import { CategoriaDetailComponent } from './categoria-detail.component';
import { CategoriaPopupComponent } from './categoria-dialog.component';
import { CategoriaDeletePopupComponent } from './categoria-delete-dialog.component';

export const categoriaRoute: Routes = [
    {
        path: 'categoria',
        component: CategoriaComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'arteApplicationTemplateApp.categoria.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'categoria/:id',
        component: CategoriaDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'arteApplicationTemplateApp.categoria.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const categoriaPopupRoute: Routes = [
    {
        path: 'categoria-new',
        component: CategoriaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'arteApplicationTemplateApp.categoria.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'categoria/:id/edit',
        component: CategoriaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'arteApplicationTemplateApp.categoria.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'categoria/:id/delete',
        component: CategoriaDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'arteApplicationTemplateApp.categoria.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
