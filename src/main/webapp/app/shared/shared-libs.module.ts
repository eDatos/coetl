// FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { Autosize } from 'ng-autosize';
import { NgJhipsterModule } from 'ng-jhipster';
import { CookieModule } from 'ngx-cookie';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import {
    AutoCompleteModule,
    ButtonModule,
    CalendarModule,
    CheckboxModule,
    DataTableModule,
    FileUploadModule,
    InputTextareaModule,
    ListboxModule,
    OrderListModule,
    SelectButtonModule,
} from 'primeng/primeng';

import { DEFAULT_LANGUAGE } from './language/language.constants';

@NgModule({
    declarations: [
        Autosize,
    ],
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
        OrderListModule,
        CheckboxModule,
        InputTextareaModule,
        FileUploadModule,
        DataTableModule
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
        OrderListModule,
        CheckboxModule,
        InputTextareaModule,
        Autosize,
        FileUploadModule,
        DataTableModule
    ]
})
export class ArteApplicationTemplateSharedLibsModule { }
