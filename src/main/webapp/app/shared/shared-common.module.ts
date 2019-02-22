import { LOCALE_ID, NgModule } from '@angular/core';
import { Title } from '@angular/platform-browser';

import {
    CoetlSharedLibsModule,
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
    PaginationComponent,
    SpinnerComponent,
    StepsComponent,
    StickyTableHeaderDirective,
    TriInputSwitchComponent,
    CurrencyComponent,
    MultiInputComponent,
    AcDatePipe
} from '.';

@NgModule({
    imports: [CoetlSharedLibsModule],
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
        PaginationComponent,
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
        }
    ],
    exports: [
        CoetlSharedLibsModule,
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
        PaginationComponent,
        AutofocusDirective,
        CurrencyComponent,
        SpinnerComponent,
        StepsComponent,
        MultiInputComponent,
        AcDatePipe
    ]
})
export class CoetlSharedCommonModule {}
