import { InjectionToken } from '@angular/core';

export interface CurrencyConfig {
    precision: number;
    prefix: string;
    suffix: string;
    decimal: string;
    thousands: string;
}

export let CURRENCY_CONFIG = new InjectionToken<CurrencyConfig>('currency.config');
