import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CoetlSharedModule } from '../../shared';
import { EtlService } from './etl.service';
import { etlRoute, EtlResolvePagingParams } from './etl.route';
import { EtlComponent } from './etl.component';
import { EtlSearchComponent } from './etl-search';
import { EtlResolve } from './etl-resolve.service';
import { EtlFormComponent } from './etl-form.component';
import { EtlDeleteDialogComponent } from './etl-delete-dialog.component';

const ENTITY_STATES = [...etlRoute];

@NgModule({
    imports: [CoetlSharedModule, RouterModule.forRoot(ENTITY_STATES, { useHash: true })],
    declarations: [EtlComponent, EtlSearchComponent, EtlFormComponent, EtlDeleteDialogComponent],
    entryComponents: [EtlDeleteDialogComponent],
    providers: [EtlService, EtlResolve, EtlResolvePagingParams]
})
export class CoetlEtlModule {}
