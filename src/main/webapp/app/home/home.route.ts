import { Route } from '@angular/router';

export const HOME_ROUTE: Route = {
    path: '',
    redirectTo: '/user-management', // FIXME Redirigir a la página inicial correspondiente. Debe ser accesible para todos los roles
    pathMatch: 'full',
    data: {
        roles: [],
        pageTitle: 'home.title'
    }
};
