import { Component, OnInit, forwardRef, Input, ViewChild, Output, EventEmitter } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { Calendar } from 'primeng/primeng';

export const AC_CALENDAR_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => CalendarComponent),
    multi: true
};

class Locale {
    firstDayOfWeek: number;
    dayNames: string[];
    dayNamesShort: string[];
    dayNamesMin: string[];
    monthNames: string[];
    monthNamesShort: string[];
    today: string;
    clear: string;
}

class LocaleEs {
    static getLocale(): Locale {
        return {
            firstDayOfWeek: 0,
            dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
            dayNamesShort: ['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'],
            dayNamesMin: ['D', 'L', 'M', 'X', 'J', 'V', 'S'],
            monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
            monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
            today: 'Hoy',
            clear: 'Reiniciar'
        }
    };
}

@Component({
    selector: 'ac-calendar',
    templateUrl: 'calendar.component.html',
    styleUrls: ['calendar.component.scss'],
    providers: [AC_CALENDAR_VALUE_ACCESSOR]
})
export class CalendarComponent implements ControlValueAccessor, OnInit {

    _date;

    locale = LocaleEs.getLocale();

    @Input()
    name = 'calendar';

    @Input()
    placeholder = 'Fecha'

    @Input()
    required = false;

    @Input()
    dateFormat = 'dd/mm/yy';

    @Input()
    minDate: Date;

    @Input()
    maxDate: Date;

    @Output()
    private onSelect: EventEmitter<any> = new EventEmitter();

    @ViewChild(Calendar)
    private calendar: Calendar;

    private onModelChange: (_: any) => {};

    private onModelTouched: Function = () => { };

    constructor(private translateService: TranslateService) { }

    ngOnInit() {
    }

    onSelectMethod($event) {
        this.onSelect.emit($event);
    }

    // Value Accesor
    writeValue(value: any): void {
        this._date = value;
    }

    @Input()
    get date(): any {
        return this._date;
    }

    set date(value) {
        this._date = value;
        this.onModelChange(this._date);
    }

    registerOnChange(fn: any): void {
        this.onModelChange = fn;
    }

    registerOnTouched(fn: any): void {
        this.onModelTouched = fn;
    }

}
