import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { CoetlSharedModule } from '../../shared';
import { CategoriaService } from '.';

@NgModule({
    imports: [
        CoetlSharedModule,
    ],
    providers: [
        CategoriaService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoetlCategoriaModule {}
