import { Observable } from 'rxjs/Rx';
import { DatePipe } from '@angular/common';
import { ParamLoader } from './param-loader';

export abstract class BaseEntityFilter {

    private loaders: ParamLoader[] = [];

    constructor(
        public datePipe?: DatePipe
    ) {
        this.registerParameters();
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

    fromQueryParams(params: any): Observable<any> {
        const filtersToRefresh = this.loaders.filter((loader) => {
            const paramValue = params[loader.paramName];
            if (!paramValue) {
                loader.clearFilter();
                return false;
            }

            if (!loader.needsToRecoverFilterFromServer || loader.needsToRecoverFilterFromServer(paramValue)) {
                loader.updateFilterFromParam(paramValue);
                return false;
            }

            return true;
        }).filter((loader) => !!loader.createSubscription);

        if (filtersToRefresh.length === 0) {
            return Observable.create((observer) => {
                observer.next();
                observer.complete();
            });
        }

        const observableList = filtersToRefresh.map((loader) => loader.recoverFilterFromServer(params[loader.paramName]));
        const callbackList = filtersToRefresh.map((loader) => loader.updateFilterAndSuggestionsFromServer);
        return Observable.zip(...observableList, (...responsesArray: Array<any>): void => {
            responsesArray.forEach((response, index) => {
                callbackList[index](response);
            });
        });
    }

    protected abstract registerParameters(): void;

    protected registerParam(loader: ParamLoader): void {
        this.loaders.push(loader);
    }

    reset() {
        this.loaders.forEach((loader) => loader.clearFilter());
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
