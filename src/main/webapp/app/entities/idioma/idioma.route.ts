import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { IdiomaComponent } from './idioma.component';
import { IdiomaDetailComponent } from './idioma-detail.component';
import { IdiomaPopupComponent } from './idioma-dialog.component';
import { IdiomaDeletePopupComponent } from './idioma-delete-dialog.component';

export const idiomaRoute: Routes = [
    {
        path: 'idioma',
        component: IdiomaComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'arteApplicationTemplateApp.idioma.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'idioma/:id',
        component: IdiomaDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'arteApplicationTemplateApp.idioma.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const idiomaPopupRoute: Routes = [
    {
        path: 'idioma-new',
        component: IdiomaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'arteApplicationTemplateApp.idioma.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'idioma/:id/edit',
        component: IdiomaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'arteApplicationTemplateApp.idioma.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'idioma/:id/delete',
        component: IdiomaDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'arteApplicationTemplateApp.idioma.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
