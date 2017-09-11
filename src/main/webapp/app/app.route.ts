import { Route } from '@angular/router';

import { NavbarComponent } from './layouts';
import { UserRouteAccessService } from './shared/index';

export const navbarRoute: Route = {
    path: '',
    component: NavbarComponent,
    data: {
        roles: ['ROLE_ADMIN', 'ROLE_USER']
    },
    canActivate: [UserRouteAccessService],
    outlet: 'navbar'
};
