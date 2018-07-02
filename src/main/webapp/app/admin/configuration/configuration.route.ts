import { Route } from '@angular/router';

import { JhiConfigurationComponent } from './configuration.component';
import { UserRouteAccessService } from '../../shared/index';
import { HERRAMIENTAS_ROLES } from '../../shared/auth/permission.service';

export const configurationRoute: Route = {
    path: 'jhi-configuration',
    component: JhiConfigurationComponent,
    data: {
        pageTitle: 'configuration.title',
        roles: HERRAMIENTAS_ROLES
    },
    canActivate: [UserRouteAccessService]
};
