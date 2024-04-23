import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CoetlSharedModule } from '../../shared';
import {
    ComputationalThreadResolvePagingParams,
    computationalThreadRoute
} from './computational-thread.route';
import { ComputationalThreadService } from './computational-thread.service';
import { ComputationalThreadComponent } from './computational-thread.component';
import { ComputationalThreadFormComponent } from './form/computational-thread-form.component';
import { ComputationalThreadResolve } from './computational-thread-resolve.service';
import { ComputationalThreadSearchComponent } from './search';
import { ComputationalThreadEtlListComponent } from './list/computational-thread-etl-list.component';
import { ComputationalThreadExecutionListComponent } from './execution/computational-thread-execution-list.component';
import { ComputationalThreadConfirmExecutionDialogComponent } from './execution/dialog/computational-thread-confirm-execution-dialog.component';

const ENTITY_STATES = [...computationalThreadRoute];

@NgModule({
    imports: [CoetlSharedModule, RouterModule.forRoot(ENTITY_STATES, { useHash: true })],
    declarations: [
        ComputationalThreadComponent,
        ComputationalThreadFormComponent,
        ComputationalThreadSearchComponent,
        ComputationalThreadEtlListComponent,
        ComputationalThreadExecutionListComponent,
        ComputationalThreadConfirmExecutionDialogComponent
    ],
    entryComponents: [ComputationalThreadConfirmExecutionDialogComponent],
    providers: [
        ComputationalThreadService,
        ComputationalThreadResolve,
        ComputationalThreadResolvePagingParams
    ]
})
export class CoetlComputationalThreadModule {}
