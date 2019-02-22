import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CoetlSharedModule } from '../shared';

import { SettingsComponent, accountState } from '.';

@NgModule({
    imports: [CoetlSharedModule, RouterModule.forRoot(accountState, { useHash: true })],
    declarations: [SettingsComponent],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoetlAccountModule {}
