// FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)

import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ArteApplicationTemplateSharedModule } from '../shared';

import {

    SettingsComponent,
    accountState
} from '.';

@NgModule({
    imports: [
        ArteApplicationTemplateSharedModule,
        RouterModule.forRoot(accountState, { useHash: true })
    ],
    declarations: [

        SettingsComponent
    ],
    providers: [

    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArteApplicationTemplateAccountModule { }
