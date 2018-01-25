// FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)
import { LOCALE_ID, NgModule } from '@angular/core';
import { Title } from '@angular/platform-browser';

import {
    ArteApplicationTemplateSharedLibsModule,
    AutocompleteComponent,
    HelpTooltipComponent,
    JhiAlertErrorComponent,
    JhiLanguageHelper,
    MakeFixedRoomDirective,
    OrderListComponent,
    PaginationComponent,
    StickyTableHeaderDirective,
    TriInputSwitchComponent,
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
        HelpTooltipComponent,
        PaginationComponent
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
        HelpTooltipComponent,
        PaginationComponent
    ]
})
export class ArteApplicationTemplateSharedCommonModule { }
