import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SecretariaLibroSharedModule } from '../../shared';
import {
    OperacionService,
    OperacionPopupService,
    OperacionComponent,
    OperacionDialogComponent,
    operacionRoute,
    OperacionResolvePagingParams,
} from './';
import { OperacionSearchComponent } from './operacion-search/index';

const ENTITY_STATES = [
    ...operacionRoute,
];

@NgModule({
    imports: [
        SecretariaLibroSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        OperacionSearchComponent,
        OperacionComponent,
        OperacionDialogComponent,
    ],
    entryComponents: [
        OperacionComponent,
        OperacionDialogComponent,
    ],
    providers: [
        OperacionService,
        OperacionPopupService,
        OperacionResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SecretariaLibroOperacionModule { }
