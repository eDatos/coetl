import { Routes, Route } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ErrorComponent } from './error.component';
import { NotFoundComponent } from './not-found.component';

export const errorRoute: Routes = [
    {
        path: 'error',
        component: ErrorComponent,
        data: {
            roles: [],
            pageTitle: 'error.title'
        },
    },
    {
        path: 'accessdenied',
        component: ErrorComponent,
        data: {
            roles: [],
            pageTitle: 'error.title',
            error403: true
        },
    },
    {
        path: 'notfound',
        component: NotFoundComponent
    }
];

export const notFoundRoute: Route = {
    path: '**',
    redirectTo: 'notfound'
};
