import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { CoetlFileModule } from './file/file.module';
import { CoetlEtlModule } from './etl/etl.module';
import { CoetlExternalItemModule } from './external-item/external-item.module';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        CoetlFileModule,
        CoetlEtlModule,
        CoetlExternalItemModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoetlEntityModule {}
