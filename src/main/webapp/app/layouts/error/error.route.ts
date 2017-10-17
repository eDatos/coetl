import { Routes, Route } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ErrorComponent } from './error.component';

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
        component: ErrorComponent,
        data: {
            roles: [],
            pageTitle: 'error.title',
            error404: true
        }
    },
    {
        path: 'non-existent-user',
        component: ErrorComponent,
        data: {
            roles: [],
            pageTitle: 'error.title',
            nonExistentUser: true
        }
    },
    {
        path: 'blocked',
        component: ErrorComponent,
        data: {
            roles: [],
            pageTitle: 'error.title',
            blockedUser: true
        }
    }
];

export const notFoundRoute: Route = {
    path: '**',
    redirectTo: 'notfound'
};
