import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared/auth/user-route-access-service';
import { TextAreaFormComponent } from './textarea-form.component';

// FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)

export const textAreaRoute: Routes = [
    {
        path: 'textarea',
        component: TextAreaFormComponent,
        resolve: {

        },
        data: {
            pageTitle: 'arteApplicationTemplateApp.operacion.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
];
