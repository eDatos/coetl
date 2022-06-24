import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CoetlSharedModule } from '../shared';
/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */

import {
    adminState,
    AuditsComponent,
    LogsComponent,
    JhiMetricsMonitoringModalComponent,
    JhiMetricsMonitoringComponent,
    HealthDialogComponent,
    HealthEditDialogComponent,
    HealthDeleteDialogComponent,
    HealthComponent,
    JhiConfigurationComponent,
    AuditsService,
    JhiConfigurationService,
    HealthService,
    JhiMetricsService,
    LogsService,
    AuditsResolvePagingParams,
    GlobalParameterComponent
} from '.';
import { GloablParameterService } from './global-parameters/global-parameter.service';
import { ParameterResolvePagingParams } from './global-parameters/global-parameter.route';
import { GlobalParameterDialogComponent } from './global-parameters/global-parameter-dialog/global-parameter-dialog.component';
import { GlobalParameterDeleteDialogComponent } from './global-parameters/global-parameter-dialog/global-parameter-delete-dialog.component';

@NgModule({
    imports: [
        CoetlSharedModule,
        RouterModule.forRoot(adminState, { useHash: true })
        /* jhipster-needle-add-admin-module - JHipster will add admin modules here */
    ],
    declarations: [
        AuditsComponent,
        LogsComponent,
        JhiConfigurationComponent,
        HealthComponent,
        HealthDialogComponent,
        HealthEditDialogComponent,
        HealthDeleteDialogComponent,
        JhiMetricsMonitoringComponent,
        JhiMetricsMonitoringModalComponent,
        GlobalParameterComponent,
        GlobalParameterDialogComponent,
        GlobalParameterDeleteDialogComponent
    ],
    entryComponents: [
        HealthDialogComponent,
        HealthEditDialogComponent,
        HealthDeleteDialogComponent,
        JhiMetricsMonitoringModalComponent,
        GlobalParameterDialogComponent,
        GlobalParameterDeleteDialogComponent
    ],
    providers: [
        AuditsService,
        JhiConfigurationService,
        HealthService,
        JhiMetricsService,
        LogsService,
        AuditsResolvePagingParams,
        GloablParameterService,
        ParameterResolvePagingParams
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoetlAdminModule {}
