import { Route } from '@angular/router';

import { HealthComponent } from './health.component';
import { UserRouteAccessService } from '../../shared';
import { HERRAMIENTAS_ROLES } from '../../shared';

export const healthRoute: Route = {
    path: 'jhi-health',
    component: HealthComponent,
    data: {
        pageTitle: 'health.title',
        roles: HERRAMIENTAS_ROLES
    },
    canActivate: [UserRouteAccessService]
};
