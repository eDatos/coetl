import { Route } from '@angular/router';
import { EdatosNavbarComponent } from './edatos-navbar.component';
import { UserRouteAccessService } from '../../shared';

export const edatosNavbarRoute: Route = {
    path: '',
    component: EdatosNavbarComponent,
    data: {},
    canActivate: [UserRouteAccessService],
    outlet: 'edatos-navbar'
};
