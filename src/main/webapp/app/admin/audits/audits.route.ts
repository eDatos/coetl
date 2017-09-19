import { Route } from '@angular/router';

import { AuditsComponent } from './audits.component';
import { UserRouteAccessService } from '../../shared/index';

export const auditsRoute: Route = {
    path: 'audits',
    component: AuditsComponent,
    data: {
        pageTitle: 'audits.title',
        operaciones: 'LEER:TEST'
    },
    canActivate: [UserRouteAccessService]
};
