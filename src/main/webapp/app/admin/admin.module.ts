import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CoetlSharedModule } from '../shared';
/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */

import {
    adminState,
    AuditsComponent,
    UserMgmtComponent,
    UserDeleteDialogComponent,
    UserMgmtFormComponent,
    UserMgmtDeleteDialogComponent,
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
    UserResolvePagingParams,
    UserModalService,
    AuditsResolvePagingParams
} from '.';
import { UserSearchComponent } from './user-management/user-search';

@NgModule({
    imports: [
        CoetlSharedModule,
        RouterModule.forRoot(adminState, { useHash: true })
        /* jhipster-needle-add-admin-module - JHipster will add admin modules here */
    ],
    declarations: [
        AuditsComponent,
        UserSearchComponent,
        UserMgmtComponent,
        UserDeleteDialogComponent,
        UserMgmtFormComponent,
        UserMgmtDeleteDialogComponent,
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
        UserMgmtFormComponent,
        UserMgmtDeleteDialogComponent,
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
        AuditsResolvePagingParams,
        UserResolvePagingParams,
        UserModalService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoetlAdminModule {}
