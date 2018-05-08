import { Component, ElementRef, Input, forwardRef, Optional, Inject } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { CURRENCY_CONFIG, CurrencyConfig } from './currency.config';

export const AC_CURRENCY_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => CurrencyComponent),
    multi: true
};

@Component({
    selector: 'ac-currency',
    templateUrl: 'currency.component.html',
    styleUrls: ['currency.component.scss'],
    providers: [AC_CURRENCY_VALUE_ACCESSOR]
})
export class CurrencyComponent implements ControlValueAccessor {

    private _acModel;

    private options: CurrencyConfig = {
        precision: 2,
        prefix: '',
        suffix: ' €',
        decimal: ',',
        thousands: '.'
    }

    constructor(@Optional() @Inject(CURRENCY_CONFIG) private currencyConfig: CurrencyConfig) {
        if (currencyConfig) {
            this.options = currencyConfig;
        }
    }

    onFocusMethod() {
        this._acModel = this.transformForUser(this._acModel);
    }

    onBlurMethod() {
        this._acModel = this.transformForUser(this._acModel);
    }

    @Input()
    get acModel(): any {
        return this._acModel;
    }

    set acModel(value) {
        this._acModel = value;
        this.onModelChange(this.parse(value));
    }

    writeValue(value: any): void {
        this._acModel = this.transform(value);
    }

    registerOnChange(fn: Function): void {
        this.onModelChange = fn;
    }

    registerOnTouched(fn: Function): void {
        this.onModelTouched = fn;
    }

    private onModelChange: Function = () => { };

    private onModelTouched: Function = () => { };

    private transformForUser(value: string): string {
        const num = this.parse(value);
        return this.transform(num);
    }

    private transform(value: number): string {
        if (value === null || value === undefined) {
            return null;
        }

        const stringValue = value.toString();

        let integerPart = stringValue;
        let decimalPart = '';
        const indexOfDecimal = stringValue.indexOf('.');
        if (indexOfDecimal >= 0) {
            integerPart = stringValue.slice(0, indexOfDecimal)
            decimalPart = stringValue.slice(indexOfDecimal + 1);
        }

        integerPart = integerPart.replace(/^0*/g, '').replace(/\B(?=(\d{3})+(?!\d))/g, this.options.thousands);

        if (integerPart === '') {
            integerPart = '0';
        }

        if (this.options.precision > 0) {
            decimalPart = decimalPart + '0'.repeat(this.options.precision - decimalPart.length);
        }

        return this.options.prefix + integerPart + this.options.decimal + decimalPart + this.options.suffix;
    }

    private parse(value: string): number {
        if (value === null || value === undefined || value === '') {
            return null;
        }

        const parsedValue = value.replace(this.options.prefix, '')
            .replace(this.options.suffix, '')
            .replace(new RegExp('\\' + this.options.thousands, 'g'), '')
            .replace(this.options.decimal, '.');

        return parseFloat(parsedValue);
    }
}
