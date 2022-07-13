import { Routes } from '@angular/router';

import {
    auditsRoute,
    configurationRoute,
    healthRoute,
    logsRoute,
    metricsRoute,
    parameterRoute
} from '.';

import { UserRouteAccessService } from '../shared';

const ADMIN_ROUTES = [
    auditsRoute,
    configurationRoute,
    healthRoute,
    logsRoute,
    metricsRoute,
    parameterRoute
];

export const adminState: Routes = [
    {
        path: '',
        data: {},
        canActivate: [UserRouteAccessService],
        children: ADMIN_ROUTES
    }
];
