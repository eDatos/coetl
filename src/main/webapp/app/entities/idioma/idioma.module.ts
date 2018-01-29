import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { ArteApplicationTemplateSharedModule } from '../../shared';
import { IdiomaService } from './';

@NgModule({
    imports: [
        ArteApplicationTemplateSharedModule,
    ],
    providers: [
        IdiomaService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArteApplicationTemplateIdiomaModule {}
