// FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)
import { DatePipe } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import {
    ArteApplicationTemplateSharedCommonModule,
    ArteApplicationTemplateSharedLibsModule,
    AuthServerProvider,
    CalendarComponent,
    CSRFService,
    EntityListEmptyComponent,
    FileUploadComponent,
    GenericModalService,
    HasAnyOperacionDirective,
    LoginService,
    Principal,
    RolService,
    SideMenuComponent,
    SplitButtonComponent,
    StateStorageService,
    UserService,
} from './';

@NgModule({
    imports: [
        ArteApplicationTemplateSharedLibsModule,
        ArteApplicationTemplateSharedCommonModule,
        RouterModule
    ],
    declarations: [
        HasAnyOperacionDirective,
        EntityListEmptyComponent,
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
        RolService,
        UserService,
        DatePipe,
        GenericModalService,
    ],
    entryComponents: [],
    exports: [
        ArteApplicationTemplateSharedCommonModule,
        HasAnyOperacionDirective,
        DatePipe,
        EntityListEmptyComponent,
        SplitButtonComponent,
        CalendarComponent,
        SideMenuComponent,
        FileUploadComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class ArteApplicationTemplateSharedModule { }
