import { Route } from '@angular/router';

import { JhiHealthCheckComponent } from './health.component';
import { UserRouteAccessService } from '../../shared/index';

export const healthRoute: Route = {
    path: 'jhi-health',
    component: JhiHealthCheckComponent,
    data: {
        pageTitle: 'health.title',
        roles: 'ADMIN'
    },
    canActivate: [UserRouteAccessService]
};
