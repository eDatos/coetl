import { DatePipe } from '@angular/common';

export abstract class BaseEntityFilter {

    constructor(
        public datePipe?: DatePipe,
    ) {
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
