import { Route } from '@angular/router';

import { LogsComponent } from './logs.component';
import { UserRouteAccessService, Rol } from '../../shared/index';

export const logsRoute: Route = {
    path: 'logs',
    component: LogsComponent,
    data: {
        pageTitle: 'logs.title',
        roles: [Rol.ADMIN]
    },
    canActivate: [UserRouteAccessService]
};
