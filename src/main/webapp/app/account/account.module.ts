import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SecretariaLibroSharedModule } from '../shared';

import {

    SettingsComponent,
    accountState
} from './';

@NgModule({
    imports: [
        SecretariaLibroSharedModule,
        RouterModule.forRoot(accountState, { useHash: true })
    ],
    declarations: [

        SettingsComponent
    ],
    providers: [

    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SecretariaLibroAccountModule { }
