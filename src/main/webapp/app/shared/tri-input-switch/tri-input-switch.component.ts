import { Component, OnInit, Input, forwardRef } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';

@Component({
    selector: 'ac-tri-input-switch',
    templateUrl: './tri-input-switch.component.html',
    styleUrls: ['./tri-input-switch.component.scss'],
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => TriInputSwitchComponent),
            multi: true,
        }
    ]
})

export class TriInputSwitchComponent implements OnInit, ControlValueAccessor {

    @Input()
    nullable: boolean;

    @Input()
    _selectedValue: boolean;

    @Input()
    required: boolean;

    @Input()
    disabled: boolean;

    options: any[];

    constructor() { }

    ngOnInit() {
        this.options = [
            {
                label: 'Sí', value: true
            },
            {
                label: 'No', value: false
            }
        ];
    }

    get selectedValue() {
        return this._selectedValue;
    }

    set selectedValue(value) {
        if (!this.required && this._selectedValue === value && this._selectedValue != null) {
            this.clear();
        } else {
            this._selectedValue = value;
        }
        this.propagateChange(this._selectedValue);
    }

    propagateChange = (_: any) => { };

    writeValue(value: any) {
        if (value !== undefined) {
            this.selectedValue = value;
        }
    }

    clear() {
        this.selectedValue = null;
    }

    registerOnChange(fn) {
        this.propagateChange = fn;
    }

    registerOnTouched() { }
}
