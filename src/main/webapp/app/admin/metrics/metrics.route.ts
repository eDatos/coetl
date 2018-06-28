import { Route } from '@angular/router';

import { JhiMetricsMonitoringComponent } from './metrics.component';
import { UserRouteAccessService, Rol } from '../../shared/index';

export const metricsRoute: Route = {
    path: 'jhi-metrics',
    component: JhiMetricsMonitoringComponent,
    data: {
        pageTitle: 'metrics.title',
        roles: [Rol.ADMIN]
    },
    canActivate: [UserRouteAccessService]
};
