import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { Execution, Result } from '../../execution/execution.model';
import { Principal, ResponseWrapper, ITEMS_PER_PAGE_SM } from '../../../shared';
import { EtlService } from '../etl.service';

@Component({
    selector: 'ac-etl-execution-list',
    templateUrl: 'etl-execution-list.component.html'
})
export class EtlExecutionListComponent implements OnInit, OnDestroy {
    public static EVENT_NAME = 'etlExecutionListModification';

    @Input() idEtl: number;

    page: number;
    totalItems: number;
    itemsPerPage: number;
    links: any;
    predicate: string;
    reverse: boolean;

    currentAccount: Account;
    executions: Execution[];
    eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private etlService: EtlService,
        private principal: Principal,
        private parseLinks: JhiParseLinks,
        private translateService: TranslateService
    ) {
        this.page = 1;
        this.itemsPerPage = ITEMS_PER_PAGE_SM;
        this.reverse = false;
        this.predicate = 'planningDate';
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.loadAll();
        this.registerChangesInEtlExecution();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    existExecutions(): boolean {
        return !!this.executions && !!this.executions.length;
    }

    getTypeName(execution: Execution): string {
        return this.translateService.instant(`coetlApp.execution.type.${execution.type}`);
    }

    getResultName(execution: Execution): string {
        return this.translateService.instant(`coetlApp.execution.result.${execution.result}`);
    }

    getResultBadgeClass(execution: Execution): any {
        return {
            'badge-success': execution.result === Result.SUCCESS,
            'badge-danger': execution.result === Result.FAILED,
            'badge-warning': execution.result === Result.WAITING,
            'badge-primary': execution.result === Result.RUNNING
        };
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    transition() {
        const req = {
            page: this.page,
            size: this.itemsPerPage
        };
        this.loadAll(req);
    }

    clear() {
        const req = {
            page: 0,
            size: this.itemsPerPage
        };
        this.loadAll(req);
    }

    trackId(index: number, item: Execution) {
        return item.id;
    }

    private loadAll(req?: { page; size }) {
        const requestOption = req
            ? req
            : {
                  page: this.page,
                  size: this.itemsPerPage
              };
        this.etlService
            .findAllExecutions(this.idEtl, {
                page: requestOption.page - 1,
                size: requestOption.size,
                sort: this.sort()
            })
            .subscribe((response: ResponseWrapper) =>
                this.onSuccess(response.json, response.headers)
            );
    }

    private registerChangesInEtlExecution() {
        this.eventSubscriber = this.eventManager.subscribe(
            EtlExecutionListComponent.EVENT_NAME,
            (response) => this.loadAll()
        );
    }

    private onSuccess(data: Execution[], headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.executions = data;
    }
}
