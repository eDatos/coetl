import './vendor.ts';

import { NgModule, APP_INITIALIZER } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { SecretariaLibroSharedModule, UserRouteAccessService, AuthServerProvider } from './shared';
import { SecretariaLibroAdminModule } from './admin/admin.module';
import { SecretariaLibroAccountModule } from './account/account.module';
import { SecretariaLibroEntityModule } from './entities/entity.module';

import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

// jhipster-needle-angular-add-module-import JHipster will add new module here

import { SecretariaConfigModule, ConfigService } from './config';

import {
    JhiMainComponent,
    LayoutRoutingModule,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent
} from './layouts';

export function init(configService: ConfigService, authServerProvider: AuthServerProvider) {
    return () => {
        const promise: Promise<boolean> = new Promise((resolve, reject) => {
            if (authServerProvider.getToken()) {
                resolve(true);
            } else {
                const config = configService.getConfig();
                window.location.href = config.cas.login + '?service=' + encodeURIComponent(config.cas.applicationHome);
            }
        });
        return promise;
    }
}

@NgModule({
    imports: [
        BrowserModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        SecretariaLibroSharedModule,
        SecretariaLibroAdminModule,
        SecretariaLibroAccountModule,
        SecretariaLibroEntityModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        SecretariaConfigModule,
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent
    ],
    providers: [
        {
            'provide': APP_INITIALIZER,
            'useFactory': init,
            'deps': [ConfigService, AuthServerProvider],
            'multi': true
        },
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService,
    ],
    bootstrap: [JhiMainComponent]
})
export class SecretariaLibroAppModule { }
