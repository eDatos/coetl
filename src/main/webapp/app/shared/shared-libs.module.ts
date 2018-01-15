// FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)

import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgJhipsterModule } from 'ng-jhipster';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { CookieModule } from 'ngx-cookie';
import { DEFAULT_LANGUAGE } from './language/language.constants';
import { CalendarModule, AutoCompleteModule, ButtonModule, SelectButtonModule, ListboxModule, OrderListModule } from 'primeng/primeng';

@NgModule({
    imports: [
        NgbModule.forRoot(),
        NgJhipsterModule.forRoot({
            // set below to true to make alerts look like toast
            alertAsToast: true,
            i18nEnabled: true,
            defaultI18nLang: DEFAULT_LANGUAGE
        }),
        InfiniteScrollModule,
        CookieModule.forRoot(),
        BrowserAnimationsModule,
        CalendarModule,
        AutoCompleteModule,
        ButtonModule,
        SelectButtonModule,
        ListboxModule,
        OrderListModule
    ],
    exports: [
        FormsModule,
        HttpModule,
        CommonModule,
        NgbModule,
        NgJhipsterModule,
        InfiniteScrollModule,
        AutoCompleteModule,
        CalendarModule,
        ButtonModule,
        SelectButtonModule,
        ListboxModule,
        OrderListModule
    ]
})
export class ArteApplicationTemplateSharedLibsModule { }
