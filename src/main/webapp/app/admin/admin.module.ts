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
    AuditsResolvePagingParams
} from '.';

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
        JhiMetricsMonitoringModalComponent
    ],
    entryComponents: [
        HealthDialogComponent,
        HealthEditDialogComponent,
        HealthDeleteDialogComponent,
        JhiMetricsMonitoringModalComponent
    ],
    providers: [
        AuditsService,
        JhiConfigurationService,
        HealthService,
        JhiMetricsService,
        LogsService,
        AuditsResolvePagingParams
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoetlAdminModule {}
