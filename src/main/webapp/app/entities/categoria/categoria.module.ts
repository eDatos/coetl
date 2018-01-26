import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { ArteApplicationTemplateSharedModule } from '../../shared';
import { CategoriaService } from './';

@NgModule({
    imports: [
        ArteApplicationTemplateSharedModule,
    ],
    providers: [
        CategoriaService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArteApplicationTemplateCategoriaModule {}
