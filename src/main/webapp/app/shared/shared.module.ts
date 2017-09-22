import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router'
import { DatePipe } from '@angular/common';

import {
    SecretariaLibroSharedLibsModule,
    SecretariaLibroSharedCommonModule,
    CSRFService,
    AuthServerProvider,
    AccountService,
    UserService,
    StateStorageService,
    LoginService,
    LoginModalService,
    Principal,
    HasAnyOperacionDirective,
    JhiLoginModalComponent,
    RolService,
    EntityListEmptyComponent,
    SplitButtonComponent,
    AutocompleteComponent,
    SideMenuComponent
} from './';

@NgModule({
    imports: [
        SecretariaLibroSharedLibsModule,
        SecretariaLibroSharedCommonModule,
        RouterModule
    ],
    declarations: [
        JhiLoginModalComponent,
        HasAnyOperacionDirective,
        EntityListEmptyComponent,
        SplitButtonComponent,
        AutocompleteComponent,
        SideMenuComponent
    ],
    providers: [
        LoginService,
        LoginModalService,
        AccountService,
        StateStorageService,
        Principal,
        CSRFService,
        AuthServerProvider,
        RolService,
        UserService,
        DatePipe,
    ],
    entryComponents: [JhiLoginModalComponent],
    exports: [
        SecretariaLibroSharedCommonModule,
        JhiLoginModalComponent,
        HasAnyOperacionDirective,
        DatePipe,
        EntityListEmptyComponent,
        SplitButtonComponent,
        AutocompleteComponent,
        SideMenuComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class SecretariaLibroSharedModule { }
