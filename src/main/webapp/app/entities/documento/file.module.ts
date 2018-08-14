import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FileService } from '.';
import { CoetlSharedModule } from '../../shared';

const ENTITY_STATES = [];

@NgModule({
    imports: [CoetlSharedModule, RouterModule.forRoot(ENTITY_STATES, { useHash: true })],
    declarations: [],
    entryComponents: [],
    providers: [FileService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoetlFileModule {}
