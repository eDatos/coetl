import { Route } from '@angular/router';

import { JhiMetricsMonitoringComponent } from './metrics.component';
import { UserRouteAccessService } from '../../shared/index';
import { HERRAMIENTAS_ROLES } from '../../shared';

export const metricsRoute: Route = {
    path: 'jhi-metrics',
    component: JhiMetricsMonitoringComponent,
    data: {
        pageTitle: 'metrics.title',
        roles: HERRAMIENTAS_ROLES
    },
    canActivate: [UserRouteAccessService]
};
