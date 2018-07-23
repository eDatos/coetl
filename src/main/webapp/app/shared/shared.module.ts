// FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)
import { DatePipe } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import {
    AcAlertService,
    CoetlSharedCommonModule,
    CoetlSharedLibsModule,
    AuthServerProvider,
    CalendarComponent,
    CSRFService,
    EntityListEmptyComponent,
    FileUploadComponent,
    GenericModalService,
    AuditInfoComponent,
    LoginService,
    Principal,
    ScrollService,
    SideMenuComponent,
    SplitButtonComponent,
    StateStorageService,
    UserService,
    ProfileService,
    PermissionService
} from '.';

@NgModule({
    imports: [
        CoetlSharedLibsModule,
        CoetlSharedCommonModule,
        RouterModule
    ],
    declarations: [
        EntityListEmptyComponent,
        AuditInfoComponent,
        SplitButtonComponent,
        CalendarComponent,
        SideMenuComponent,
        FileUploadComponent
    ],
    providers: [
        LoginService,
        StateStorageService,
        Principal,
        CSRFService,
        AuthServerProvider,
        UserService,
        ProfileService,
        DatePipe,
        GenericModalService,
        AcAlertService,
        ScrollService,
        PermissionService
    ],
    entryComponents: [],
    exports: [
        CoetlSharedCommonModule,
        DatePipe,
        EntityListEmptyComponent,
        AuditInfoComponent,
        SplitButtonComponent,
        CalendarComponent,
        SideMenuComponent,
        FileUploadComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class CoetlSharedModule { }
