import './vendor.ts';

import { NgModule, APP_INITIALIZER } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { CoetlSharedModule, UserRouteAccessService, AuthServerProvider } from './shared';
import { CoetlHomeModule } from './home/home.module';
import { CoetlAdminModule } from './admin/admin.module';
import { CoetlAccountModule } from './account/account.module';
import { CoetlEntityModule } from './entities/entity.module';

import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

import { DEFAULT_LANG } from './app.constants';
import { TranslateService } from '@ngx-translate/core';

// jhipster-needle-angular-add-module-import JHipster will add new module here

import { CoetlConfigModule, ConfigService } from './config';

import {
    JhiMainComponent,
    LayoutRoutingModule,
    NavbarComponent,
    EdatosNavbarComponent,
    ErrorComponent,
    notFoundRoute
} from './layouts';

const APP_ROUTES = [notFoundRoute];

export function init(configService: ConfigService, authServerProvider: AuthServerProvider) {
    return () => {
        const promise: Promise<boolean> = new Promise((resolve, reject) => {
            if (authServerProvider.getToken()) {
                resolve(true);
            } else {
                const config = configService.getConfig();
                window.location.href =
                    config.cas.login + '?service=' + encodeURIComponent(config.cas.service);
            }
        });
        return promise;
    };
}

export function initTranslations(translateService: TranslateService) {
    translateService.setDefaultLang(DEFAULT_LANG);
    return () => translateService.use(DEFAULT_LANG).toPromise();
}

@NgModule({
    imports: [
        BrowserModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        CoetlSharedModule,
        CoetlHomeModule,
        CoetlAdminModule,
        CoetlAccountModule,
        CoetlEntityModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        CoetlConfigModule,
        RouterModule.forRoot(APP_ROUTES, { useHash: true })
    ],
    declarations: [JhiMainComponent, NavbarComponent, EdatosNavbarComponent, ErrorComponent],
    providers: [
        {
            provide: APP_INITIALIZER,
            useFactory: init,
            deps: [ConfigService, AuthServerProvider],
            multi: true
        },
        {
            provide: APP_INITIALIZER,
            useFactory: initTranslations,
            deps: [TranslateService],
            multi: true
        },
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [JhiMainComponent]
})
export class CoetlAppModule {}
