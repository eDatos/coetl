import { AfterViewInit, Component, EventEmitter, forwardRef, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { AutoComplete } from 'primeng/primeng';
import { EcitUtils } from '../utils/EcitUtils';

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
export class AutocompleteComponent implements ControlValueAccessor, OnInit, AfterViewInit {

    // Atributos internos
    @ViewChild(AutoComplete)
    protected autoComplete: AutoComplete;

    private originalProperties = [];

    public debouncedMode;

    protected _selectedSuggestions: any;

    protected _suggestions: any[];

    public filteredSuggestions: any[];

    private focusMustOpenPanel = true;

    public field: string = null;

    private myNewLabel = '';

    public internalItemTemplate: Function;

    private autoCompleteOnKeydown: Function;

    // Parametros opcionales
    @Output()
    private onClear: EventEmitter<any> = new EventEmitter();

    @Output()
    private onBlur: EventEmitter<any> = new EventEmitter();

    @Output()
    private onSelect: EventEmitter<any> = new EventEmitter();

    @Output()
    private onUnselect: EventEmitter<any> = new EventEmitter();

    @Input()
    public createNonFound = false;

    @Input()
    public deleteOnBackspace = true;

    @Input()
    public emptyMessage = this.translateService.instant('entity.list.empty.detail');

    @Input()
    public placeholder: string = null;

    @Input()
    public minLength = 0;

    @Input()
    public required = false;

    @Input()
    public disabled = false;

    @Input()
    public multiple = false;

    // Parametros obligatorios
    @Input()
    protected properties: string[];

    @Output()
    protected completeMethod: EventEmitter<any> = new EventEmitter();

    constructor(protected translateService: TranslateService) { }

    // FIXME Con multiple=false field es obligatorio.
    ngOnInit() {
        if (this.properties === undefined || this.properties.length === 0) {
            throw new Error('properties is required');
        }

        if (this.createNonFound && this.properties.length > 1) {
            throw new Error('is not possible to create new elements if several fields are showed');
        }

        if (this.properties.length === 1) {
            this.field = this.properties[0];
        }
        this.originalProperties = this.properties;

        this.internalItemTemplate = this.itemTemplate;
        this.debouncedMode = this.completeMethod.observers.length > 0;
        this.placeholder = this.placeholder || (this.debouncedMode ? this.translateService.instant('entity.list.empty.writeForSuggestions') : null);
    }

    protected onModelChange: Function = () => { };

    private onModelTouched: Function = () => { };

    @Input()
    public itemTemplate: Function = (item) => {
        return this.originalProperties.map((property) => item[property]).join(' ');
    }

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

    ngAfterViewInit() {
        this.autoComplete.onInputBlur = this.onInputBlur.bind(this);
        this.autoComplete.isDropdownClick = this.isDropdownClick;

        this.autoCompleteOnKeydown = this.autoComplete.onKeydown.bind(this.autoComplete);
        this.autoComplete.onKeydown = this.onKeydown.bind(this);
    }

    // Se sobreescribe el método de la librería para evitar error al comprobar si un padre null tiene la clase
    isDropdownClick(event) {
        return false;
    }

    // Override of autoComplete.onInputBlur to patch trailing spaces issue (https://github.com/primefaces/primeng/issues/4332)
    onInputBlur(event) {
        this.autoComplete.focus = false;
        this.autoComplete.onModelTouched();
        this.autoComplete.onBlur.emit(event);

        if (this.autoComplete.forceSelection) {
            let valid = false;
            const inputValue = event.target.value.toLowerCase(); // .trim();

            if (this.autoComplete.suggestions) {
                for (const suggestion of this.autoComplete.suggestions) {
                    const itemValue = this.autoComplete.field ? this.autoComplete.objectUtils.resolveFieldData(suggestion, this.autoComplete.field) : this.itemTemplate(suggestion);
                    if (itemValue && inputValue === itemValue.toLowerCase()) {
                        valid = true;
                        break;
                    }
                }
            }

            if (!valid) {
                if (this.autoComplete.multiple) {
                    this.autoComplete.multiInputEL.nativeElement.value = '';
                } else {
                    this.autoComplete.value = null;
                    this.autoComplete.inputEL.nativeElement.value = '';
                }

                this.autoComplete.onModelChange(this.autoComplete.value);
            }
        }
    }

    // Partial override of keydown method so we can avoid to delete on backspace
    onKeydown(event) {
        const BACKSPACE = 8;
        if (this.deleteOnBackspace || event.which !== BACKSPACE) {
            this.autoCompleteOnKeydown(event);
        }
    }

    onCompleteMethod($event) {
        this.completeMethod.emit($event);
        this.filteredSuggestions = this.getFilteredSuggestions($event.query);
    }

    itsNewSuggestion(item) {
        return this.myNewLabel === item[this.field] ? this.translateService.instant('entity.list.empty.createNewIitem') : '';
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

        if (this.properties) {
            filteredSuggestions = this.filterByProperties(filteredSuggestions, query);
        }

        if (this.createNonFound) {
            filteredSuggestions = this.addNonFound(filteredSuggestions, query);
        }

        return filteredSuggestions;
    }

    addNonFound(filteredSuggestions, query) {
        if (query && filteredSuggestions && !filteredSuggestions.some((suggestion) => suggestion[this.field] === query)) {
            this.myNewLabel = query;
            filteredSuggestions.push(this.buildNewSuggestion(query));
        }
        return filteredSuggestions;
    }

    private buildNewSuggestion(fieldValue) {
        const newSuggestion = {};
        newSuggestion[this.field] = fieldValue;
        return newSuggestion;
    }

    filterByProperties(suggestions: any[], query: string) {
        return suggestions
            .filter((suggestion) => {
                if (this.properties.length > 0) {
                    return this.properties.findIndex((property) => {
                        return this.queryContainsValue(query, suggestion[property])
                    }) !== -1;
                } else {
                    return this.queryContainsValue(query, suggestion)
                }
            });
    }

    private queryContainsValue(query: string, value: string): boolean {
        if (!value) { return false; }
        return EcitUtils.removeDiacritics(value.toUpperCase()).indexOf(EcitUtils.removeDiacritics(query.toUpperCase())) !== -1;
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
        this.onBlur.emit($event);
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
        let processedSuggestions = suggestions;
        if (this.originalProperties.length > 1 && !this.multiple) {
            processedSuggestions = suggestions.map((suggestion) => {
                return Object.assign({}, suggestion, { pleaseDoNotOverlapThisField: this.itemTemplate(suggestion) });
            });
            this.field = 'pleaseDoNotOverlapThisField';
            this.properties = [this.field];
        }
        this._suggestions = processedSuggestions;
        this.filteredSuggestions = this.getFilteredSuggestions(this.getQueryValue());
        this.autoComplete.noResults = !this.filteredSuggestions.length;
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
