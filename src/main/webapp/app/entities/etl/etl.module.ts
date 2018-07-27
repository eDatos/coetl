import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CoetlSharedModule } from '../../shared';
import { EtlService } from './etl.service';
import { etlRoute, EtlResolvePagingParams } from './etl.route';
import { EtlComponent } from './etl.component';
import { EtlSearchComponent } from './etl-search';
import { EtlResolve } from './etl-resolve.service';

const ENTITY_STATES = [...etlRoute];

@NgModule({
    imports: [CoetlSharedModule, RouterModule.forRoot(ENTITY_STATES, { useHash: true })],
    declarations: [EtlComponent, EtlSearchComponent],
    entryComponents: [],
    providers: [EtlService, EtlResolve, EtlResolvePagingParams]
})
export class CoetlEtlModule {}
