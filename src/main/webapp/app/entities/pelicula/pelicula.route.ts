import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';
import { UserRouteAccessService, ITEMS_PER_PAGE } from '../../shared';
import { PeliculaDeletePopupComponent } from './pelicula-delete-dialog.component';
import { PeliculaFormComponent } from './pelicula-form.component';
import { PeliculaComponent } from './pelicula.component';
import { PeliculaResolve } from './pelicula.resolve';
import { PELICULA_ROLES } from '../../shared';
import { BASE_DECIMAL } from '../../app.constants';

@Injectable()
export class PeliculaResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        const itemsPerPage = route.queryParams['size'] ? route.queryParams['size'] : ITEMS_PER_PAGE;
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort),
            itemsPerPage: parseInt(itemsPerPage, BASE_DECIMAL)
      };
    }
}

export const peliculaRoute: Routes = [
    {
        path: 'pelicula',
        component: PeliculaComponent,
        resolve: {
            'pagingParams': PeliculaResolvePagingParams,
        },
        data: {
            roles: PELICULA_ROLES,
            pageTitle: 'coetlApp.pelicula.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'pelicula/:id',
        component: PeliculaFormComponent,
        resolve: {
            pelicula: PeliculaResolve
        },
        data: {
            roles: PELICULA_ROLES,
            pageTitle: 'coetlApp.pelicula.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'pelicula-new',
        component: PeliculaFormComponent,
        data: {
            roles: PELICULA_ROLES,
            pageTitle: 'coetlApp.pelicula.home.title'
        },
        canActivate: [UserRouteAccessService],
    },
    {
        path: 'pelicula/:id/edit',
        component: PeliculaFormComponent,
        resolve: {
            pelicula: PeliculaResolve
        },
        data: {
            roles: PELICULA_ROLES,
            pageTitle: 'coetlApp.pelicula.home.title'
        },
        canActivate: [UserRouteAccessService],
    }
];

export const peliculaPopupRoute: Routes = [
    {
        path: 'pelicula/:id/delete',
        component: PeliculaDeletePopupComponent,
        data: {
            roles: PELICULA_ROLES,
            pageTitle: 'coetlApp.pelicula.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
