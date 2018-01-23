import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ActorComponent } from './actor.component';
import { ActorDetailComponent } from './actor-detail.component';
import { ActorPopupComponent } from './actor-dialog.component';
import { ActorDeletePopupComponent } from './actor-delete-dialog.component';

export const actorRoute: Routes = [
    {
        path: 'actor',
        component: ActorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'arteApplicationTemplateApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'actor/:id',
        component: ActorDetailComponent,
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
