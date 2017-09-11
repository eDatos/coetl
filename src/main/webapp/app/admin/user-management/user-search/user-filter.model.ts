import { OnInit } from '@angular/core';
import { EntityFilter } from '../../../shared/index';

export class UserFilter implements EntityFilter {

    constructor(
        public name?: string,
        public includeDeleted = false,
    ) {
    }

    fromQueryParams(params: any) {
        // const filter = new UserFilter();
        if (params['name']) {
            this.name = params['name'];
        }
        if (params['includeDeleted']) {
            this.includeDeleted = params['includeDeleted'] === 'true';
        }
    }

    reset() {
        this.name = '';
        this.includeDeleted = false;
    }

    toQuery() {
        return this.name;
    }
}
