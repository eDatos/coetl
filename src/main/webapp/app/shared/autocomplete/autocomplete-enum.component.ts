import { Component, OnInit, Input } from '@angular/core';
import { AutocompleteComponent } from '.';
import { buildProvider } from '..';
import { Genero } from '../../entities/actor';

@Component({
    selector: 'ac-autocomplete-enum',
    templateUrl: 'autocomplete.component.html',
    styleUrls: ['autocomplete.component.scss'],
    providers: [buildProvider(AutocompleteEnumComponent)]
})
export class AutocompleteEnumComponent extends AutocompleteComponent implements OnInit {

    @Input()
    public enumType;

    @Input()
    public translationPath: string;

    ngOnInit() {
        if (this.completeMethod.observers.length > 0) {
            throw new Error('completeMethod is not supported on ac-autocomplete-enum');
        }

        if (this.properties !== undefined) {
            throw new Error('properties is not supported on ac-autocomplete-enum');
        }

        if (this.suggestions !== undefined) {
            throw new Error('suggestions is not supported on ac-autocomplete-enum');
        }

        if (this.enumType === undefined) {
            throw new Error('enumType is required on ac-autocomplete-enum');
        }

        if (this.translationPath === undefined) {
            throw new Error('translationPath is required on ac-autocomplete-enum');
        }

        this.properties = ['value'];
        this.suggestions = Object.keys(this.enumType).map((key) => Object.assign({}, {
            id: key, value: this.translateService.instant(this.translationPath + key)
        }));

        super.ngOnInit();
    }

    private findSuggestionByEnum(enumeration: any) {
        return this.suggestions.filter((suggestion) => suggestion.id === enumeration)[0];
    }

    writeValue(value: any): void {
        if (value instanceof Array) {
            this._selectedSuggestions = value.map((element) => this.findSuggestionByEnum(element));
        } else {
            this._selectedSuggestions = this.findSuggestionByEnum(value);
        }
    }

    @Input()
    get selectedSuggestions(): any {
        return this._selectedSuggestions;
    }

    set selectedSuggestions(value) {
        this._selectedSuggestions = value;
        if (value instanceof Array) {
            this.onModelChange(this._selectedSuggestions.map((suggestion) => suggestion.id));
        } else {
            this.onModelChange(this._selectedSuggestions.id);
        }
    }
}
