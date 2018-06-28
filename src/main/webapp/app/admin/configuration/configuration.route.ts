import { Route } from '@angular/router';

import { JhiConfigurationComponent } from './configuration.component';
import { UserRouteAccessService, Rol } from '../../shared/index';

export const configurationRoute: Route = {
    path: 'jhi-configuration',
    component: JhiConfigurationComponent,
    data: {
        pageTitle: 'configuration.title',
        roles: [Rol.ADMIN]
    },
    canActivate: [UserRouteAccessService]
};
