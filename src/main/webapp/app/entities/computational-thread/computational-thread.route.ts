import { Routes } from '@angular/router';

import { READ_ETL_ROLES, MANAGE_ETL_ROLES, UserRouteAccessService } from '../../shared';
import { ComputationalThreadComponent } from './computational-thread.component';
import { ComputationalThreadFormComponent } from './form/computational-thread-form.component';

export const computationalThreadRoute: Routes = [
    {
        path: 'computational-threads',
        component: ComputationalThreadComponent,
        data: {
            pageTitle: 'computationalThread.pageTitle',
            roles: READ_ETL_ROLES
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'computational-threads-new',
        component: ComputationalThreadFormComponent,
        data: {
            roles: MANAGE_ETL_ROLES,
            pageTitle: 'computationalThread.pageTitle'
        },
        canActivate: [UserRouteAccessService]
    }
];
