import { Component, forwardRef, Input, HostBinding } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

export const AC_MULTI_INPUT_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => MultiInputComponent),
    multi: true
};

@Component({
    selector: 'ac-multi-input',
    templateUrl: 'multi-input.component.html',
    styleUrls: ['./multi-input.component.scss'],
    providers: [AC_MULTI_INPUT_VALUE_ACCESSOR]
})
export class MultiInputComponent implements ControlValueAccessor {
    private _values: string[];

    public placeholder = this.translateService.instant('entity.list.empty.multiInput');

    @Input()
    field: string;

    @Input()
    @HostBinding('class.disabled')
    disabled: boolean;

    @Input()
    allowDuplicate = false;

    @Input()
    addOnBlur = true;

    @Input()
    addOnTab = true;

    private onModelChange: Function = () => { };
    private onModelTouched: Function = () => { };

    constructor(private translateService: TranslateService) { }

    /* ControlValueAccessor */

    writeValue(value: any): void {
        this.values = value;
    }

    @Input()
    get values(): any {
        return this._values;
    }

    set values(value) {
        this._values = value;
        this.onModelChange(this._values);
    }

    registerOnChange(fn: Function): void {
        this.onModelChange = fn;
    }

    registerOnTouched(fn: Function): void {
        this.onModelTouched = fn;
    }
}
