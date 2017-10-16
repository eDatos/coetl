import { NgModule, LOCALE_ID } from '@angular/core';
import { Title } from '@angular/platform-browser';

import {
    SecretariaLibroSharedLibsModule,
    JhiLanguageHelper,
    JhiAlertErrorComponent,
    StickyTableHeaderDirective,
    TriInputSwitchComponent,
    MakeFixedRoomDirective,
    AutocompleteComponent,
    OrderListComponent,
    HelpTooltipComponent
} from './';

@NgModule({
    imports: [
        SecretariaLibroSharedLibsModule
    ],
    declarations: [
        JhiAlertErrorComponent,
        StickyTableHeaderDirective,
        TriInputSwitchComponent,
        MakeFixedRoomDirective,
        AutocompleteComponent,
        OrderListComponent,
        HelpTooltipComponent
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
        JhiAlertErrorComponent,
        StickyTableHeaderDirective,
        TriInputSwitchComponent,
        MakeFixedRoomDirective,
        AutocompleteComponent,
        OrderListComponent,
        HelpTooltipComponent
    ]
})
export class SecretariaLibroSharedCommonModule { }
