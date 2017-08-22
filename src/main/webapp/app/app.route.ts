import { Route } from '@angular/router';

import { NavbarComponent } from './layouts';
import { UserRouteAccessService } from './shared/index';

export const navbarRoute: Route = {
    path: '',
    component: NavbarComponent,
    data: {
        authorities: ['ROLE_ADMIN', 'ROLE_USER']
    },
    canActivate: [UserRouteAccessService],
    outlet: 'navbar'
};
