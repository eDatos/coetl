import { Route } from '@angular/router';

import { JhiHealthCheckComponent } from './health.component';
import { UserRouteAccessService } from '../../shared/index';
import { HERRAMIENTAS_ROLES } from '../admin-permission.service';

export const healthRoute: Route = {
    path: 'jhi-health',
    component: JhiHealthCheckComponent,
    data: {
        pageTitle: 'health.title',
        roles: HERRAMIENTAS_ROLES
    },
    canActivate: [UserRouteAccessService]
};
