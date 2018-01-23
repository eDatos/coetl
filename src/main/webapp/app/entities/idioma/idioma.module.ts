import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ArteApplicationTemplateSharedModule } from '../../shared';
import {
    IdiomaService,
    IdiomaPopupService,
    IdiomaComponent,
    IdiomaDetailComponent,
    IdiomaDialogComponent,
    IdiomaPopupComponent,
    IdiomaDeletePopupComponent,
    IdiomaDeleteDialogComponent,
    idiomaRoute,
    idiomaPopupRoute,
} from './';

const ENTITY_STATES = [
    ...idiomaRoute,
    ...idiomaPopupRoute,
];

@NgModule({
    imports: [
        ArteApplicationTemplateSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        IdiomaComponent,
        IdiomaDetailComponent,
        IdiomaDialogComponent,
        IdiomaDeleteDialogComponent,
        IdiomaPopupComponent,
        IdiomaDeletePopupComponent,
    ],
    entryComponents: [
        IdiomaComponent,
        IdiomaDialogComponent,
        IdiomaPopupComponent,
        IdiomaDeleteDialogComponent,
        IdiomaDeletePopupComponent,
    ],
    providers: [
        IdiomaService,
        IdiomaPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArteApplicationTemplateIdiomaModule {}
