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
    styleUrls: ['autocomplete.component.scss'],
    providers: [AC_AUTOCOMPLETE_VALUE_ACCESSOR]
})
// Sample where the calls are made to the service
// <ac-autocomplete name="operacion" (completeMethod)="filterOperaciones($event)" [debouncedMode]="true" [(ngModel)]="rol.operaciones" [suggestions]="operaciones"
// [itemTemplate]="operacionItemTemplate"></ac-autocomplete>
//
// Sample where array is static and is filtered via properties
// <ac-autocomplete name="operacion" [propertiesToQuery]="['accion', 'sujeto']"  [(ngModel)]="rol.operaciones"
// [suggestions]="operaciones" [itemTemplate]="operacionItemTemplate"></ac-autocomplete>
export class AutocompleteComponent implements ControlValueAccessor, OnInit {

    @Input()
    private propertiesToQuery: string[];

    @Input()
    public debouncedMode = false;

    @Output()
    private completeMethod: EventEmitter<any> = new EventEmitter();

    @Output()
    private onSelect: EventEmitter<any> = new EventEmitter();

    @Output()
    private onUnselect: EventEmitter<any> = new EventEmitter();

    @Output()
    private onClear: EventEmitter<any> = new EventEmitter();

    private _selectedSuggestions: any;

    private _suggestions: any[];

    public filteredSuggestions: any[];

    @ViewChild(AutoComplete)
    private autoComplete: AutoComplete;

    private focusMustOpenPanel = true;

    @Input()
    public minLength = 3;

    @Input()
    emptyMessage = this.translateService.instant('entity.list.empty.detail');

    @Input()
    public multiple = true;

    @Input()
    public field: string = null;

    @Input()
    public placeholder: string = null;

    @Input()
    public required = false;

    @Input()
    public createNonFound = false;

    private myNewLabel = '';

    public internalItemTemplate: Function;

    private onModelChange: Function = () => { };

    private onModelTouched: Function = () => { };

    @Input()
    public itemTemplate: Function = (item) => item;

    @Input()
    private compareWith: Function = (selectedSuggestion, existingSuggestion) => {
        if (selectedSuggestion && existingSuggestion) {
            if (!selectedSuggestion.id) { console.error('selectedSuggestion don\'t have defined id', selectedSuggestion) };
            if (!existingSuggestion.id) { console.error('existingSuggestion don\'t have defined id', existingSuggestion) };
            return selectedSuggestion.id === existingSuggestion.id;
        } else {
            return selectedSuggestion === existingSuggestion;
        }
    }

    constructor(private translateService: TranslateService) { }

    ngOnInit() {
        this.internalItemTemplate = this.itemTemplate;
    }

    onCompleteMethod($event) {
        this.completeMethod.emit($event);
        this.filteredSuggestions = this.getFilteredSuggestions($event.query);
        if ($event.query && this.createNonFound && this.filteredSuggestions && !this.filteredSuggestions.some((s) => s[this.field] === $event.query)) {
            this.myNewLabel = $event.query;
            const newLabel = {};
            newLabel[this.field] = $event.query;
            this.filteredSuggestions.push(newLabel);
        }
    }

    itsNewLabel(item) {
        return this.myNewLabel === item[this.field] ? this.translateService.instant('entity.list.empty.createNewItem') : '';
    }

    onSelectMethod($event) {
        this.onSelect.emit($event);
        this.focusMustOpenPanel = false;
    }

    onUnselectMethod($event) {
        this.onUnselect.emit($event);
    }

    onClearMethod($event) {
        this.onClear.emit($event);
    }

    getFilteredSuggestions(query) {
        let filteredSuggestions = this.suggestions ? this.suggestions.slice() : [];

        if (this._selectedSuggestions instanceof Array) {
            filteredSuggestions = this.excludeAlreadySelectedSuggestions(filteredSuggestions);
        }

        if (this.propertiesToQuery) {
            filteredSuggestions = this.filterByPropertiesToQuery(filteredSuggestions, query);
        }

        return filteredSuggestions;
    }

    filterByPropertiesToQuery(suggestions: any[], query: string) {
        return suggestions
            .filter((suggestion) => {
                if (this.propertiesToQuery.length > 0) {
                    return this.propertiesToQuery.findIndex((property) =>
                        suggestion[property].toUpperCase().indexOf(query.toUpperCase()) !== -1
                    ) !== -1;
                } else {
                    return suggestion.toUpperCase().indexOf(query.toUpperCase()) !== -1
                }
            });
    }

    excludeAlreadySelectedSuggestions(suggestions: any[]) {
        return suggestions
            .filter((suggestion) => {
                const self = this;
                return !self._selectedSuggestions || self._selectedSuggestions.findIndex((selectedSuggestion) => self.compareWith(selectedSuggestion, suggestion)) === -1;
            });
    }

    // https://github.com/primefaces/primeng/issues/745
    handleDropdownSuggestions($event) {
        this.focusMustOpenPanel = !this.autoComplete.panelVisible;
    }

    handleOnFocusOutSuggestions($event) {
        this.focusMustOpenPanel = true;
        this.onModelTouched();
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
        if (this.multiple) {
            return this.autoComplete.multiInputEL ? this.autoComplete.multiInputEL.nativeElement.value : '';
        } else {
            return this.autoComplete.inputEL ? this.autoComplete.inputEL.nativeElement.value : '';
        }
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
    get selectedSuggestions(): any {
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
