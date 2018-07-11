// FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)
import { LOCALE_ID, NgModule } from '@angular/core';
import { Title } from '@angular/platform-browser';

import {
    ArteApplicationTemplateSharedLibsModule,
    AutocompleteComponent,
    AutocompleteShortListComponent,
    AutocompleteLongListComponent,
    AutocompleteEnumComponent,
    AutofocusDirective,
    HelpTooltipComponent,
    JhiAlertErrorComponent,
    JhiLanguageHelper,
    MakeFixedRoomDirective,
    OrderListComponent,
    ItemsPerPageComponent,
    SpinnerComponent,
    StepsComponent,
    StickyTableHeaderDirective,
    TriInputSwitchComponent,
    CurrencyComponent,
    MultiInputComponent,
    AcDatePipe
} from '.';

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
        AutocompleteShortListComponent,
        AutocompleteLongListComponent,
        AutocompleteEnumComponent,
        OrderListComponent,
        HelpTooltipComponent,
        ItemsPerPageComponent,
        AutofocusDirective,
        CurrencyComponent,
        SpinnerComponent,
        StepsComponent,
        MultiInputComponent,
        AcDatePipe
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
        AutocompleteShortListComponent,
        AutocompleteLongListComponent,
        AutocompleteEnumComponent,
        OrderListComponent,
        HelpTooltipComponent,
        ItemsPerPageComponent,
        AutofocusDirective,
        CurrencyComponent,
        SpinnerComponent,
        StepsComponent,
        MultiInputComponent,
        AcDatePipe
    ]
})
export class ArteApplicationTemplateSharedCommonModule { }
