import { Observable } from 'rxjs/Rx';
import { DatePipe } from '@angular/common';
import { ParamLoader } from './param-loader';
import { Executable, execute } from '..';
import { ExecuterHelper } from '../executer/executer-helper';

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

    fromQueryParams(params: any): Observable<void> {
        this.getSyncParams(params);
        return Observable.create((observer) => {
            if (this.loaders.length === 0) {
                observer.next();
                observer.complete();
                return;
            }

            const helper = new ExecuterHelper(observer, this.loaders.length);
            this.loaders.forEach((loader) => {
                if (!params[loader.paramName]) {
                    helper.notifyDone();
                    return;
                }

                const paramList = params[loader.paramName].split(',')
                const containAll = paramList.every((paramElement) => {
                    return this[loader.collectionName].find((suggestion) => suggestion[loader.entityProperty] === loader.parseFromString(paramElement)) !== undefined;
                }, this);

                if (containAll) {
                    helper.notifyDone();
                    return;
                }

                loader.load(paramList, helper.notifyDone.bind(helper));
            });
        });
    }

    protected getSyncParams(params: any): void { };

    protected registerAsyncParameters(): void { };

    protected registerAsyncParam(loader: ParamLoader): void {
        this.loaders.push(loader);
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
