import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { SettingsComponent } from './settings.component';

export const settingsRoute: Routes = [
    {
        path: 'settings',
        component: SettingsComponent,
        data: {
            operaciones: 'LEER:CUENTA_PROPIA',
            pageTitle: 'global.menu.account.settings'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'settings/edit',
        component: SettingsComponent,
        data: {
            operaciones: 'EDITAR:CUENTA_PROPIA',
            pageTitle: 'global.menu.account.settings'
        },
        canActivate: [UserRouteAccessService]
    }
];
