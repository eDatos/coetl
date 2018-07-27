import { BaseEntityFilter, EntityFilter } from '../../../shared';
import { Type } from '../etl.model';

export class EtlFilter extends BaseEntityFilter implements EntityFilter {
    public allTypes: string[];

    public code: string;
    public name: string;
    public type: Type;
    public includeDeleted = false;

    constructor() {
        super();
        this.allTypes = Object.keys(Type);
    }

    protected registerParameters() {
        this.registerParam({
            paramName: 'code',
            updateFilterFromParam: (param) => (this.code = param),
            clearFilter: () => (this.code = null)
        });

        this.registerParam({
            paramName: 'name',
            updateFilterFromParam: (param) => (this.name = param),
            clearFilter: () => (this.name = null)
        });

        this.registerParam({
            paramName: 'type',
            updateFilterFromParam: (param) => (this.type = this.convertParamToType(param)),
            clearFilter: () => (this.type = null)
        });

        this.registerParam({
            paramName: 'includeDeleted',
            updateFilterFromParam: (param) => (this.includeDeleted = param === 'true'),
            clearFilter: () => (this.includeDeleted = false)
        });
    }

    private convertParamToType(param: any): Type {
        const currentKey = this.allTypes.find((key) => key === param);
        return currentKey ? Type[currentKey] : undefined;
    }

    getCriterias() {
        const criterias = [];
        if (this.code) {
            criterias.push(`CODE ILIKE '%${this.code}%'`);
        }
        if (this.name) {
            criterias.push(`NAME ILIKE '%${this.name}%'`);
        }
        if (this.type) {
            criterias.push(`TYPE EQ '${this.type}'`);
        }

        return criterias;
    }
}
