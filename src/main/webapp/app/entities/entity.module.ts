import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { CoetlDocumentoModule } from './documento/documento.module';
import { CoetlEtlModule } from './etl/etl.module';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        CoetlDocumentoModule,
        CoetlEtlModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoetlEntityModule {}
