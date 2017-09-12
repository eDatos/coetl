import { Component, OnInit, forwardRef, Input, ViewChild } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';

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
// <ac-autocomplete name="operacion"
// [propertiesToQuery]="['accion', 'sujeto']"
// [(ngModel)]="rol.Suggestions"
// [suggestions]="Suggestions"
// [itemTemplate]="operacionItemTemplate">
// </ac-autocomplete>
export class AutocompleteComponent implements OnInit, ControlValueAccessor {

    _selectedSuggestions: any[];

    @Input()
    propertiesToQuery: string[];

    @Input()
    suggestions: any[];

    @Input()
    itemTemplate: Function;

    filteredSuggestions: any[];

    @ViewChild(AutoComplete)
    private autoComplete: AutoComplete;

    private focusMustOpenPanel = true;

    onModelChange: Function = () => { };

    onModelTouched: Function = () => { };

    constructor() {
    }

    ngOnInit() {
        this.filteredSuggestions = this.buildFilteredSuggestions('');
        if (!this.itemTemplate) {
            this.itemTemplate = (item) => item;
        }
    }

    buildFilteredSuggestions(query) {
        return this.suggestions
            .filter((operacion) => {
                if (this.propertiesToQuery && this.propertiesToQuery.length) {
                    return this.propertiesToQuery.findIndex((property) =>
                        operacion[property].toUpperCase().indexOf(query.toUpperCase()) !== -1
                    ) !== -1;
                } else {
                    return operacion.toUpperCase().indexOf(query.toUpperCase()) !== -1
                }
            })
            .map((operacion) => operacion);
    }

    searchSuggestions(event) {
        this.filteredSuggestions = this.buildFilteredSuggestions(event.query);
    }

    handleDropdownSuggestions(event) {
        this.focusMustOpenPanel = !this.autoComplete.panelVisible;
    }

    handleOnFocusSuggestions(event) {
        setTimeout(() => {
            this.filteredSuggestions = this.buildFilteredSuggestions('');
            if (this.focusMustOpenPanel) {
                this.autoComplete.show();
            } else {
                this.autoComplete.hide();
            }
        }, 0)
    }

    handleOnFocusOutSuggestions(event) {
        this.focusMustOpenPanel = true;
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
