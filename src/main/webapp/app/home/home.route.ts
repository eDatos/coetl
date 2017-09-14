import { Route } from '@angular/router';

export const HOME_ROUTE: Route = {
    path: '',
    redirectTo: '/user-management', // FIXME Redirect to appropiate home page. Beware is accesible for all roles
    pathMatch: 'full',
    data: {
        roles: [],
        pageTitle: 'home.title'
    }
};
