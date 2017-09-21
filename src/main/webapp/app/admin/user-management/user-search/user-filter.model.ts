import { OnInit, Input } from '@angular/core';
import { EntityFilter, Rol } from '../../../shared/index';

export class UserFilter implements EntityFilter {

    public name?: string;
    public roles?: Rol[] = [];
    public includeDeleted = false;

    constructor(
        public allRoles?: Rol[]
    ) {
    }

    setAllRoles(roles: Rol[]) {
        this.allRoles = roles;
    }

    fromQueryParams(params: any) {
        if (params['name']) {
            this.name = params['name'];
        }
        if (params['roles']) {
            this.roles = this.rolesFromParam(params['roles']);
        }
        if (params['includeDeleted']) {
            this.includeDeleted = params['includeDeleted'] === 'true';
        }
    }

    reset() {
        this.name = '';
        this.roles = [];
        this.includeDeleted = false;
    }

    toQuery() {
        return this.getCriterias().join(' AND ');
    }

    toUrl() {
        return {
            name: this.name,
            roles: this.rolesToParam(),
            includeDeleted: this.includeDeleted,
        }
    }

    private rolesFromParam(param): Rol[] {
        const roles = []
        param.split(',')
            .map((id) => this.allRoles
                .filter((r) => r.id.toString() === id)
                .map((a) => roles.push(a)));
        return roles;
    }

    private rolesToParam(): string {
        if (this.roles) {
            return this.roles.map((r) => r.id).join(',')
        }
        return null;
    }

    private getCriterias() {
        const criterias = [];
        if (this.name) {
            criterias.push(`NOMBRE ILIKE '%${this.name}%'`);
            criterias.push(`APELLIDO1 ILIKE '%${this.name}%'`);
            criterias.push(`APELLIDO2 ILIKE '%${this.name}%'`);
            criterias.push(`LOGIN ILIKE '%${this.name}%'`);
        }
        if (this.roles && this.roles.length > 0) {
            criterias.push(`ROL IN (${this.rolesToParam()})`);
        }
        return criterias;
    }
}
