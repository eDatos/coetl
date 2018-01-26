// FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ArteApplicationTemplateSharedModule } from '../../shared';
import { TextAreaFormComponent, textAreaRoute } from './';

const ENTITY_STATES = [
    ...textAreaRoute,
];

@NgModule({
    imports: [
        ArteApplicationTemplateSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TextAreaFormComponent,
    ],
    entryComponents: [
        TextAreaFormComponent,
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArteApplicationTemplateTextAreaModule { }
