import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { CoetlSharedModule } from '../../shared';
import { IdiomaService } from '.';

@NgModule({
    imports: [
        CoetlSharedModule,
    ],
    providers: [
        IdiomaService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoetlIdiomaModule {}
