import { Route } from '@angular/router';

import { JhiConfigurationComponent } from './configuration.component';
import { UserRouteAccessService } from '../../shared';
import { HERRAMIENTAS_ROLES } from '../../shared';

export const configurationRoute: Route = {
    path: 'jhi-configuration',
    component: JhiConfigurationComponent,
    data: {
        pageTitle: 'configuration.title',
        roles: HERRAMIENTAS_ROLES
    },
    canActivate: [UserRouteAccessService]
};
