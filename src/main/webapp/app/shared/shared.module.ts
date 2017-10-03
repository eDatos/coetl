import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router'
import { DatePipe } from '@angular/common';

import {
    SecretariaLibroSharedLibsModule,
    SecretariaLibroSharedCommonModule,
    CSRFService,
    AuthServerProvider,
    UserService,
    StateStorageService,
    LoginService,
    Principal,
    HasAnyOperacionDirective,
    RolService,
    EntityListEmptyComponent,
    SplitButtonComponent,
    AutocompleteComponent,
    CalendarComponent,
    SideMenuComponent
} from './';

@NgModule({
    imports: [
        SecretariaLibroSharedLibsModule,
        SecretariaLibroSharedCommonModule,
        RouterModule
    ],
    declarations: [
        HasAnyOperacionDirective,
        EntityListEmptyComponent,
        SplitButtonComponent,
        AutocompleteComponent,
        CalendarComponent,
        SideMenuComponent
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
    ],
    entryComponents: [],
    exports: [
        SecretariaLibroSharedCommonModule,
        HasAnyOperacionDirective,
        DatePipe,
        EntityListEmptyComponent,
        SplitButtonComponent,
        AutocompleteComponent,
        CalendarComponent,
        SideMenuComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class SecretariaLibroSharedModule { }
