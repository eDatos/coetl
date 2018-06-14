import { Observable } from 'rxjs/Rx';
import { DatePipe } from '@angular/common';
import { FilterHelper } from './filter-helper';
import { ParamLoader } from './param-loader';

export abstract class BaseEntityFilter {

    private loaders: ParamLoader[] = [];

    constructor(
        public datePipe?: DatePipe
    ) {
        this.registerAsyncParameters();
    }

    protected updateQueryParam(id: string, params: any[], field?: string) {
        if (this[id] && (this[id].length === undefined || this[id].length > 0)) {
            if (this[id] instanceof Array) {
                params[id] = Array.from(this[id]).map((item) => this.getItemOrId(item, field)).join();
            } else if (this[id] instanceof Date) {
                params[id] = this.dateToString(this[id]);
            } else {
                params[id] = this.getItemOrId(this[id], field);
            }
        } else {
            delete params[id]
        }
    }

    getItemOrId(item: any, field?: string) {
        field = field || 'id';
        return item[field] ? item[field] : item;
    }

    toQuery() {
        return this.getCriterias().join(' AND ');
    }

    toOrQuery() {
        return this.getCriterias().join(' OR ');
    }

    abstract fromQueryParams(params: any);

    fromAsyncQueryParams(params: any): Observable<void> {
        this.fromQueryParams(params);
        return Observable.create((observer) => {
            if (this.loaders.length > 0) {
                const helper = new FilterHelper(observer, this.loaders);
                this.loaders.forEach((loader) => loader(params, helper.notifyDone.bind(helper)));
            } else {
                observer.next();
                observer.complete();
            }
        });
    }

    protected registerAsyncParameters(): void { };

    protected registerAsyncParam(carga: ParamLoader): void {
        this.loaders.push(carga);
    }

    toUrl(queryParams) {
        const obj = Object.assign({}, queryParams);
        Object.keys(this).map((id) => {
            this.updateQueryParam(id, obj)
        });
        return obj;
    }

    protected dateToString(date: Date): string {
        const dateFormat = 'dd/MM/yyyy';
        if (date && date.toString().match('../../....')) {
            return date.toString();
        }
        return this.datePipe.transform(date, dateFormat)
    }

    abstract getCriterias();
}
