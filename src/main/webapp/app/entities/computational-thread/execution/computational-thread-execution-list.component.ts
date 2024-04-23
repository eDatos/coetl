import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { ComputationalThreadExecution, Result } from '../computational-thread-execution.model';
import { ComputationalThreadService } from '../computational-thread.service';
import { ITEMS_PER_PAGE_SM, ResponseWrapper } from '../../../shared';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'ac-computational-threads-execution-list',
    templateUrl: 'computational-thread-execution-list.component.html'
})
export class ComputationalThreadExecutionListComponent implements OnInit, OnDestroy {
    public static EVENT_NAME = 'computationalThreadExecutionListModification';

    private executions: ComputationalThreadExecution[];

    @Input() idThread: number;

    public page: number;
    public totalItems: number;
    private itemsPerPage: number;
    private predicate: string;
    private reverse: boolean;
    private eventSubscriber: Subscription;

    constructor(
        private computationalThreadService: ComputationalThreadService,
        private translateService: TranslateService,
        private eventManager: JhiEventManager
    ) {
        this.page = 1;
        this.itemsPerPage = ITEMS_PER_PAGE_SM;
        this.reverse = false;
        this.predicate = 'planningDate';
    }

    ngOnInit() {
        this.loadAll();
        this.registerChangesInThreadExecution();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    private loadAll(req?: { page; size }) {
        const requestOption = req
            ? req
            : {
                  page: this.page,
                  size: this.itemsPerPage
              };
        this.computationalThreadService
            .findAllExecutions(this.idThread, {
                page: requestOption.page - 1,
                size: requestOption.size,
                sort: this.sort()
            })
            .subscribe((response: ResponseWrapper) =>
                this.onSuccess(response.json, response.headers)
            );
    }

    private onSuccess(data: ComputationalThreadExecution[], headers) {
        this.totalItems = headers.get('X-Total-Count');
        this.executions = data;
    }

    private sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    public transition() {
        const req = {
            page: this.page,
            size: this.itemsPerPage
        };
        this.loadAll(req);
    }

    public getTypeName(execution: ComputationalThreadExecution): string {
        return this.translateService.instant(
            `computationalThread.execution.type.${execution.type}`
        );
    }

    public getResultName(execution: ComputationalThreadExecution): string {
        return this.translateService.instant(
            `computationalThread.execution.result.${execution.result}`
        );
    }

    public getResultBadgeClass(execution: ComputationalThreadExecution): any {
        return {
            'badge-success': execution.result === Result.SUCCESS,
            'badge-danger': execution.result === Result.FAILED,
            'badge-warning': execution.result === Result.WAITING,
            'badge-primary': execution.result === Result.RUNNING,
            'badge-default': execution.result === Result.DUPLICATED
        };
    }

    // VALIDATIONS
    public existExecutions() {
        return this.executions && this.executions.length;
    }

    // EVENT POPUP
    private registerChangesInThreadExecution() {
        this.eventSubscriber = this.eventManager.subscribe(
            ComputationalThreadExecutionListComponent.EVENT_NAME,
            () => this.loadAll()
        );
    }
}
