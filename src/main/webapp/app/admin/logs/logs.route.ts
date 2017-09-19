import { Route } from '@angular/router';

import { LogsComponent } from './logs.component';
import { UserRouteAccessService } from '../../shared/index';

export const logsRoute: Route = {
    path: 'logs',
    component: LogsComponent,
    data: {
        pageTitle: 'logs.title',
        operaciones: 'LEER:LOGS'
    },
    canActivate: [UserRouteAccessService]
};
