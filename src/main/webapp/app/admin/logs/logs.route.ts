import { Route } from '@angular/router';

import { LogsComponent } from './logs.component';
import { UserRouteAccessService } from '../../shared/index';
import { HERRAMIENTAS_ROLES } from '../admin-permission.service';

export const logsRoute: Route = {
    path: 'logs',
    component: LogsComponent,
    data: {
        pageTitle: 'logs.title',
        roles: HERRAMIENTAS_ROLES
    },
    canActivate: [UserRouteAccessService]
};
