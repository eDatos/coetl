import { Route } from '@angular/router';

import { HomeComponent } from './';

export const HOME_ROUTE: Route = {
    path: '',
    pathMatch: 'full',
    component: HomeComponent,
    data: {
        roles: [],
        pageTitle: 'home.pageTitle'
    }
};
