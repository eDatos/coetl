import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DocumentoService } from '.';
import { ArteApplicationTemplateSharedModule } from '../../shared';

const ENTITY_STATES = [
];

@NgModule({
    imports: [
        ArteApplicationTemplateSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
    ],
    entryComponents: [
    ],
    providers: [
        DocumentoService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArteApplicationTemplateDocumentoModule { }
