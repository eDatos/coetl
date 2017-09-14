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
        return this.getCriterias().join(' AND ');
    }

    toOrQuery() {
        return this.getCriterias().join(' OR ');
    }

    getCriterias() {
        const criterias = [];
        if (this.accion) {
            criterias.push(`ACCION ILIKE '%${this.accion}%'`);
        }
        if (this.sujeto) {
            criterias.push(`SUJETO ILIKE '%${this.sujeto}%'`);
        }
        return criterias;
    }
}
