import { NgModule, LOCALE_ID } from '@angular/core';
import { Title } from '@angular/platform-browser';

import {
    SecretariaLibroSharedLibsModule,
    JhiLanguageHelper,
    FindLanguageFromKeyPipe,
    JhiAlertComponent,
    JhiAlertErrorComponent,
    StickyTableHeaderDirective,
    TriInputSwitchComponent
} from './';

@NgModule({
    imports: [
        SecretariaLibroSharedLibsModule
    ],
    declarations: [
        FindLanguageFromKeyPipe,
        JhiAlertComponent,
        JhiAlertErrorComponent,
        StickyTableHeaderDirective,
        TriInputSwitchComponent
    ],
    providers: [
        JhiLanguageHelper,
        Title,
        {
            provide: LOCALE_ID,
            useValue: 'es'
        },
    ],
    exports: [
        SecretariaLibroSharedLibsModule,
        FindLanguageFromKeyPipe,
        JhiAlertComponent,
        JhiAlertErrorComponent,
        StickyTableHeaderDirective,
        TriInputSwitchComponent
    ]
})
export class SecretariaLibroSharedCommonModule { }
