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
        this.roles = this.getRolesFromIds(this.roles.map((rol) => rol.id));
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
        const rolesIds = param.split(',').map((id) => Number(id));
        if (this.allRoles.length) {
            return this.getRolesFromIds(rolesIds);
        } else {
            return this.buildRolesFromIds(rolesIds);
        }
    }

    private getRolesFromIds(rolesIds: number[]): Rol[] {
        return this.allRoles.filter((rol) => {
            return rolesIds.findIndex((id) => rol.id === id) !== -1;
        });
    };

    private buildRolesFromIds(rolesIds: number[]): Rol[] {
        return rolesIds.map((id) => {
            const rol = new Rol();
            rol.id = id;
            return rol;
        });
    };

    private rolesToParam(): string {
        if (this.roles) {
            return this.roles.map((r) => r.id).join(',')
        }
        return null;
    }

    private getCriterias() {
        const criterias = [];
        if (this.name) {
            criterias.push(`USUARIO ILIKE '%${this.name}%'`);
        }
        if (this.roles && this.roles.length > 0) {
            criterias.push(`ROL IN (${this.rolesToParam()})`);
        }
        return criterias;
    }
}
