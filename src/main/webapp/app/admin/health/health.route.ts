import { Route } from '@angular/router';

import { JhiHealthCheckComponent } from './health.component';
import { TipoAccionOperacion, TipoSujetoOperacion } from '../../entities/operacion/index';
import { UserRouteAccessService } from '../../shared/index';

export const healthRoute: Route = {
    path: 'jhi-health',
    component: JhiHealthCheckComponent,
    data: {
        pageTitle: 'health.title',
        roles: ['ROLE_ADMIN'],
        // operaciones: [{ accion: TipoAccionOperacion.LEER, sujeto: TipoSujetoOperacion.ROL }]
    },
    canActivate: [UserRouteAccessService]
};
