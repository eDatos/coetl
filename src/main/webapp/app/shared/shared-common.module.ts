// FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)

import { NgModule, LOCALE_ID } from '@angular/core';
import { Title } from '@angular/platform-browser';

import {
    ArteApplicationTemplateSharedLibsModule,
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
        ArteApplicationTemplateSharedLibsModule
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
        ArteApplicationTemplateSharedLibsModule,
        JhiAlertErrorComponent,
        StickyTableHeaderDirective,
        TriInputSwitchComponent,
        MakeFixedRoomDirective,
        AutocompleteComponent,
        OrderListComponent,
        HelpTooltipComponent
    ]
})
export class ArteApplicationTemplateSharedCommonModule { }
