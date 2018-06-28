import { Route } from '@angular/router';

import { JhiDocsComponent } from './docs.component';
import { UserRouteAccessService, Rol } from '../../shared/index';

export const docsRoute: Route = {
    path: 'docs',
    component: JhiDocsComponent,
    data: {
        pageTitle: 'global.menu.admin.apidocs',
        roles: [Rol.ADMIN]
    },
    canActivate: [UserRouteAccessService]
};
