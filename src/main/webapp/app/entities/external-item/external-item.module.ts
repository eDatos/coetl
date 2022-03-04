import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ExternalItemService } from './external-item.service';

import { CoetlSharedModule } from '../../shared';

const ENTITY_STATES = [];

@NgModule({
    imports: [CoetlSharedModule, RouterModule.forRoot(ENTITY_STATES, { useHash: true })],
    declarations: [],
    entryComponents: [],
    providers: [ExternalItemService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoetlExternalItemModule {}
