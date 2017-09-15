import { Component, OnInit, forwardRef, Input, ViewChild, Output, EventEmitter } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { AutoComplete } from 'primeng/primeng';

export const AC_AUTOCOMPLETE_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => AutocompleteComponent),
    multi: true
};

@Component({
    selector: 'ac-autocomplete',
    templateUrl: 'autocomplete.component.html',
    providers: [AC_AUTOCOMPLETE_VALUE_ACCESSOR]
})
// Sample where the calls are made to the service
// <ac-autocomplete name="operacion" (completeMethod)="filterOperaciones($event)" [(ngModel)]="rol.operaciones" [suggestions]="operaciones"
// [itemTemplate]="operacionItemTemplate"></ac-autocomplete>
//
// Sample where array is static and is filtered via properties
// <ac-autocomplete name="operacion" [propertiesToQuery]="['accion', 'sujeto']" [debouncedMode]="false" [(ngModel)]="rol.operaciones"
// [suggestions]="operaciones" [itemTemplate]="operacionItemTemplate"></ac-autocomplete>
export class AutocompleteComponent implements ControlValueAccessor {

    @Input()
    private propertiesToQuery: string[];

    @Input()
    public debouncedMode = true;

    @Output()
    private completeMethod: EventEmitter<any> = new EventEmitter();

    private _selectedSuggestions: any[];

    private _suggestions: any[];

    public filteredSuggestions: any[];

    @ViewChild(AutoComplete)
    private autoComplete: AutoComplete;

    private focusMustOpenPanel = true;

    @Input()
    public minLength = 3;

    @Input()
    emptyMessage = this.translateService.instant('entity.list.empty');

    private onModelChange: Function = () => { };

    private onModelTouched: Function = () => { };

    @Input()
    public itemTemplate: Function = (item) => item;

    @Input()
    private compareWith: Function = (selectedSuggestion, suggestion) => {
        return selectedSuggestion && suggestion ? selectedSuggestion.id === suggestion.id : selectedSuggestion === suggestion;
    };

    constructor(private translateService: TranslateService) { }

    onCompleteMethod($event) {
        this.completeMethod.emit($event);
        this.filteredSuggestions = this.getFilteredSuggestions($event.query);
    }

    getFilteredSuggestions(query) {
        const dontIncludeAlreadySelectedSuggestions = this.suggestions
            .filter((suggestion) => {
                return !this._selectedSuggestions || this._selectedSuggestions.findIndex((selectedSuggestion) => this.compareWith(selectedSuggestion, suggestion)) === -1;
            });

        if (this.propertiesToQuery) {
            return dontIncludeAlreadySelectedSuggestions
                .filter((suggestion) => {
                    if (this.propertiesToQuery.length > 0) {
                        return this.propertiesToQuery.findIndex((property) =>
                            suggestion[property].toUpperCase().indexOf(query.toUpperCase()) !== -1
                        ) !== -1;
                    } else {
                        return suggestion.toUpperCase().indexOf(query.toUpperCase()) !== -1
                    }
                });
        } else {
            return dontIncludeAlreadySelectedSuggestions;
        }
    }

    // https://github.com/primefaces/primeng/issues/745
    handleDropdownSuggestions($event) {
        this.focusMustOpenPanel = !this.autoComplete.panelVisible;
    }

    handleOnFocusOutSuggestions($event) {
        this.focusMustOpenPanel = true;
    }

    handleOnFocusSuggestions($event) {
        const queryValue = this.getQueryValue();
        if (!this.debouncedMode || queryValue.length >= this.minLength) {
            this.filteredSuggestions = this.getFilteredSuggestions(queryValue);

            setTimeout(() => {
                if (this.focusMustOpenPanel) {
                    this.autoComplete.show();
                } else {
                    this.autoComplete.hide();
                }
            }, 0);
        }
    }

    getQueryValue() {
        return this.autoComplete.multiInputEL ? this.autoComplete.multiInputEL.nativeElement.value : '';
    }

    @Input()
    set suggestions(suggestions: any[]) {
        this._suggestions = suggestions;
        this.filteredSuggestions = this.getFilteredSuggestions(this.getQueryValue());
    }

    get suggestions(): any[] {
        return this._suggestions;
    }

    /* ControlValueAccessor */

    writeValue(value: any): void {
        this._selectedSuggestions = value;
    }

    @Input()
    get selectedSuggestions(): any[] {
        return this._selectedSuggestions;
    }

    set selectedSuggestions(value) {
        this._selectedSuggestions = value;
        this.onModelChange(this._selectedSuggestions);
    }

    registerOnChange(fn: Function): void {
        this.onModelChange = fn;
    }

    registerOnTouched(fn: Function): void {
        this.onModelTouched = fn;
    }
}
