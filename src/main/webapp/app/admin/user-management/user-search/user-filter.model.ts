import { EntityFilter } from '../../../shared';

export class UserFilter implements EntityFilter {

    public name?: string;
    public includeDeleted = false;

    constructor(
    ) {
    }

    fromQueryParams(params: any) {
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
        return this.getCriterias().join(' AND ');
    }

    toUrl() {
        return {
            name: this.name,
            includeDeleted: this.includeDeleted,
        }
    }

    private getCriterias() {
        const criterias: string[] = [];
        if (this.name) {
            const subcriterias: string[] = []
            this.name.split(' ').forEach((item) => subcriterias.push(`USUARIO ILIKE '%${item}%'`))
            criterias.push('(' + subcriterias.join(' AND ') + ')');
        }
        return criterias;
    }
}
