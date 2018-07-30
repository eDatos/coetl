import { EntityFilter, Rol } from '../../../shared';

export class UserFilter implements EntityFilter {
    allRoles: string[];

    public name?: string;
    public role?: string;
    public includeDeleted = false;

    constructor() {
        this.allRoles = Object.keys(Rol);
    }

    fromQueryParams(params: any) {
        if (params['name']) {
            this.name = params['name'];
        }
        if (params['role']) {
            this.role = this.convertParamToRole(params['role']);
        }
        if (params['includeDeleted']) {
            this.includeDeleted = params['includeDeleted'] === 'true';
        }
    }

    reset() {
        this.name = null;
        this.role = null;
        this.includeDeleted = false;
    }

    toQuery() {
        return this.getCriterias().join(' AND ');
    }

    toUrl() {
        return {
            name: this.name,
            role: this.role,
            includeDeleted: this.includeDeleted
        };
    }

    private convertParamToRole(param: any): Rol {
        const currentKey = this.allRoles.find((key) => key === param);
        return currentKey ? Rol[currentKey] : undefined;
    }

    private getCriterias() {
        const criterias: string[] = [];
        if (this.name) {
            const subcriterias: string[] = [];
            this.name.split(' ').forEach((item) => subcriterias.push(`USUARIO ILIKE '%${item}%'`));
            criterias.push('(' + subcriterias.join(' AND ') + ')');
        }
        if (this.role) {
            criterias.push(`ROLES EQ '${this.role}'`);
        }
        return criterias;
    }
}
