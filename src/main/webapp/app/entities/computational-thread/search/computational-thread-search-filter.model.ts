import { DatePipe } from '@angular/common';
import { BaseEntityFilter, EntityFilter } from '../../../shared';

export class ComputationalThreadFilter extends BaseEntityFilter implements EntityFilter {
    public code: string;
    public name: string;

    constructor(public datePipe: DatePipe) {
        super();
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
    }

    getCriterias() {
        const criterias = [];
        if (this.code) {
            criterias.push(`CODE ILIKE '%${this.code}%'`);
        }
        if (this.name) {
            criterias.push(`NAME ILIKE '%${this.name}%'`);
        }
        return criterias;
    }
}
