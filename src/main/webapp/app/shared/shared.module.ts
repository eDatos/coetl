import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
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
    AutocompleteComponent
} from './';

@NgModule({
    imports: [
        SecretariaLibroSharedLibsModule,
        SecretariaLibroSharedCommonModule
    ],
    declarations: [
        JhiLoginModalComponent,
        HasAnyOperacionDirective,
        EntityListEmptyComponent,
        SplitButtonComponent,
        AutocompleteComponent
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
        AutocompleteComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class SecretariaLibroSharedModule { }
