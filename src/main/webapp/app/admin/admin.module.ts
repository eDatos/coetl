// FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)

import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ArteApplicationTemplateSharedModule } from '../shared';
/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */

import {
    adminState,
    AuditsComponent,
    RolMgmtComponent,
    UserMgmtComponent,
    UserDeleteDialogComponent,
    UserMgmtDialogComponent,
    UserMgmtDeleteDialogComponent,
    LogsComponent,
    JhiMetricsMonitoringModalComponent,
    JhiMetricsMonitoringComponent,
    JhiHealthModalComponent,
    JhiHealthCheckComponent,
    JhiConfigurationComponent,
    JhiDocsComponent,
    AuditsService,
    JhiConfigurationService,
    JhiHealthService,
    JhiMetricsService,
    LogsService,
    RolResolvePagingParams,
    RolModalService,
    UserResolvePagingParams,
    UserModalService,
    AuditsResolvePagingParams
} from './';
import { RolMgmtDialogComponent } from './rol-management/rol-management-dialog.component';
import { RolMgmtDeleteDialogComponent, RolDeleteDialogComponent } from './rol-management/rol-management-delete-dialog.component';
import { UserSearchComponent } from './user-management/user-search/index';

@NgModule({
    imports: [
        ArteApplicationTemplateSharedModule,
        RouterModule.forRoot(adminState, { useHash: true }),
        /* jhipster-needle-add-admin-module - JHipster will add admin modules here */
    ],
    declarations: [
        AuditsComponent,
        RolMgmtComponent,
        RolMgmtDialogComponent,
        RolDeleteDialogComponent,
        RolMgmtDeleteDialogComponent,
        UserSearchComponent,
        UserMgmtComponent,
        UserDeleteDialogComponent,
        UserMgmtDialogComponent,
        UserMgmtDeleteDialogComponent,
        LogsComponent,
        JhiConfigurationComponent,
        JhiHealthCheckComponent,
        JhiHealthModalComponent,
        JhiDocsComponent,
        JhiMetricsMonitoringComponent,
        JhiMetricsMonitoringModalComponent
    ],
    entryComponents: [
        RolMgmtDialogComponent,
        RolMgmtDeleteDialogComponent,
        UserMgmtDialogComponent,
        UserMgmtDeleteDialogComponent,
        JhiHealthModalComponent,
        JhiMetricsMonitoringModalComponent,
    ],
    providers: [
        AuditsService,
        JhiConfigurationService,
        JhiHealthService,
        JhiMetricsService,
        LogsService,
        AuditsResolvePagingParams,
        RolResolvePagingParams,
        RolModalService,
        UserResolvePagingParams,
        UserModalService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArteApplicationTemplateAdminModule { }
