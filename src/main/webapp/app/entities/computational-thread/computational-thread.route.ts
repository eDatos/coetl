import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { READ_ETL_ROLES } from '../../shared';
import { ComputationalThreadComponent } from './computational-thread.component';

export const computationalThreadRoute: Routes = [
    {
        path: 'computational-threads',
        component: ComputationalThreadComponent,
        data: {
            pageTitle: 'computationalThread.title',
            roles: READ_ETL_ROLES
        },
        canActivate: [UserRouteAccessService]
    }
];
