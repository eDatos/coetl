import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgJhipsterModule } from 'ng-jhipster';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { CookieModule } from 'ngx-cookie';
import { DEFAULT_LANGUAGE } from './language/language.constants';
import { AutoCompleteModule, ButtonModule, SelectButtonModule, ListboxModule } from 'primeng/primeng';

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
        AutoCompleteModule,
        ButtonModule,
        SelectButtonModule,
        ListboxModule
    ],
    exports: [
        FormsModule,
        HttpModule,
        CommonModule,
        NgbModule,
        NgJhipsterModule,
        InfiniteScrollModule,
        AutoCompleteModule,
        ButtonModule,
        SelectButtonModule,
        ListboxModule
    ]
})
export class SecretariaLibroSharedLibsModule { }
