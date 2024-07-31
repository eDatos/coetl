import { DatePipe } from '@angular/common';
import { BaseEntityFilter, EntityFilter } from '../../../shared';

export class ComputationalThreadFilter extends BaseEntityFilter implements EntityFilter {
    public code: string;
    public name: string;
    public statisticalOperation: string;
    public lastExecution: Date;
    public lastExecutionByResult: string;
    public includeDeleted = false;

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

        this.registerParam({
            paramName: 'statisticalOperation',
            updateFilterFromParam: (param) => (this.statisticalOperation = param),
            clearFilter: () => (this.statisticalOperation = null)
        });

        this.registerParam({
            paramName: 'lastExecution',
            updateFilterFromParam: (param) => (this.lastExecution = param),
            clearFilter: () => (this.lastExecution = null)
        });

        this.registerParam({
            paramName: 'lastExecutionByResult',
            updateFilterFromParam: (param) => (this.lastExecutionByResult = param),
            clearFilter: () => (this.lastExecutionByResult = null)
        });

        this.registerParam({
            paramName: 'includeDeleted',
            updateFilterFromParam: (param) => (this.includeDeleted = param === 'true'),
            clearFilter: () => (this.includeDeleted = false)
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
        if (this.statisticalOperation) {
            criterias.push(`STATISTICAL_OPERATION ILIKE '%${this.statisticalOperation}%'`);
        }
        return criterias;
    }
}
