import { Route } from '@angular/router';
import { NavbarComponent } from './navbar.component';
import { UserRouteAccessService } from '../../shared/index';

export const navbarRoute: Route = {
    path: '',
    component: NavbarComponent,
    data: {
    },
    canActivate: [UserRouteAccessService],
    outlet: 'navbar'
};
