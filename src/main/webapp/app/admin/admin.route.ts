import { Routes } from '@angular/router';

import {
    auditsRoute,
    configurationRoute,
    docsRoute,
    healthRoute,
    logsRoute,
    metricsRoute,
    rolMgmtRoute,
    userMgmtRoute,
    userDialogRoute,
    rolDialogRoute
} from './';

import { UserRouteAccessService } from '../shared';

const ADMIN_ROUTES = [
    auditsRoute,
    configurationRoute,
    docsRoute,
    healthRoute,
    logsRoute,
    ...rolMgmtRoute,
    ...userMgmtRoute,
    metricsRoute
];

export const adminState: Routes = [{
    path: '',
    data: {
    },
    canActivate: [UserRouteAccessService],
    children: ADMIN_ROUTES
},
...rolDialogRoute,
...userDialogRoute
];
