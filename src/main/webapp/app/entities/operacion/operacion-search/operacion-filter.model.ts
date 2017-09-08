import { OnInit } from '@angular/core';
import { EntityFilter } from '../../../shared/index';

export class OperacionFilter implements EntityFilter {

    constructor(
        public accion?: string,
        public sujeto?: string,
    ) {
    }

    fromQueryParams(params: any) {
        if (params['accion']) {
            this.accion = params['accion'];
        }
        if (params['sujeto']) {
            this.sujeto = params['sujeto'];
        }
    }

    reset() {
        this.accion = '';
        this.sujeto = '';
    }

    toQuery() {
        const accionFilter = !!this.accion ? 'ACCION ILIKE \'%' + this.accion + '%\'' : '';
        const sujetoFilter = !!this.sujeto ? 'SUJETO ILIKE \'%' + this.sujeto + '%\'' : '';
        let query = accionFilter;
        query += (!!query && !!sujetoFilter ? ' AND ' : '') + sujetoFilter;
        return query;
    }
}
