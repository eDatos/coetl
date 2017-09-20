import { OnInit } from '@angular/core';
import { EntityFilter } from '../../../shared/index';

export class UserFilter implements EntityFilter {

    constructor(
        public name?: string,
        public rol?: string,
        public includeDeleted = false,
    ) {
    }

    fromQueryParams(params: any) {
        if (params['name']) {
            this.name = params['name'];
        }
        if (params['rol']) {
            this.rol = params['rol'];
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
        return this.getCriterias().join(' OR ');
    }

    private getCriterias() {
        const criterias = [];
        if (this.name) {
            criterias.push(`NOMBRE ILIKE '%${this.name}%'`);
            criterias.push(`APELLIDO1 ILIKE '%${this.name}%'`);
            criterias.push(`APELLIDO2 ILIKE '%${this.name}%'`);
            criterias.push(`LOGIN ILIKE '%${this.name}%'`);
        }
        if (this.rol) {
            criterias.push(`ROL ILIKE '%${this.rol}%'`);
        }
        return criterias;
    }
}
