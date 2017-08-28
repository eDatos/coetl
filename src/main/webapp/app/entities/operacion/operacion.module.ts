import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SecretariaLibroSharedModule } from '../../shared';
import {
    OperacionService,
    OperacionPopupService,
    OperacionComponent,
    OperacionDetailComponent,
    OperacionDialogComponent,
    OperacionPopupComponent,
    OperacionDeletePopupComponent,
    OperacionDeleteDialogComponent,
    operacionRoute,
    operacionPopupRoute,
    OperacionResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...operacionRoute,
    ...operacionPopupRoute,
];

@NgModule({
    imports: [
        SecretariaLibroSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        OperacionComponent,
        OperacionDetailComponent,
        OperacionDialogComponent,
        OperacionDeleteDialogComponent,
        OperacionPopupComponent,
        OperacionDeletePopupComponent,
    ],
    entryComponents: [
        OperacionComponent,
        OperacionDialogComponent,
        OperacionPopupComponent,
        OperacionDeleteDialogComponent,
        OperacionDeletePopupComponent,
    ],
    providers: [
        OperacionService,
        OperacionPopupService,
        OperacionResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SecretariaLibroOperacionModule {}
