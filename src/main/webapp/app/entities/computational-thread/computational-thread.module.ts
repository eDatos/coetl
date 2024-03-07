import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CoetlSharedModule } from '../../shared';
import { computationalThreadRoute } from './computational-thread.route';
import { ComputationalThreadService } from './computational-thread.service';
import { ComputationalThreadComponent } from './computational-thread.component';
import { ComputationalThreadFormComponent } from './form/computational-thread-form.component';

const ENTITY_STATES = [...computationalThreadRoute];

@NgModule({
    imports: [CoetlSharedModule, RouterModule.forRoot(ENTITY_STATES, { useHash: true })],
    declarations: [ComputationalThreadComponent, ComputationalThreadFormComponent],
    entryComponents: [],
    providers: [ComputationalThreadService]
})
export class CoetlComputationalThreadModule {}
