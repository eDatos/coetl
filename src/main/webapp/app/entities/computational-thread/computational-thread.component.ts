import { Component, OnInit, OnDestroy } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { PermissionService, ResponseWrapper } from '../../shared';
import { ComputationalThreadsBase } from './computational-thread.model';
import { ComputationalThreadService } from './computational-thread.service';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { ActivatedRoute, Router } from '@angular/router';
import { ComputationalThreadFilter, ComputationalThreadSearchComponent } from './search';
import { DatePipe } from '@angular/common';
import { ComputationalThreadFormComponent } from './form/computational-thread-form.component';
import { Subscription } from 'rxjs';
import { ComputationalThreadExecution, Result } from './computational-thread-execution.model';

@Component({
    selector: 'ac-computational-thread',
    templateUrl: './computational-thread.component.html'
})
export class ComputationalThreadComponent implements OnInit, OnDestroy {
    public computationalThreadList: ComputationalThreadsBase[];
    public page: number;
    public totalItems: number;
    public itemsPerPage: number;
    private links: any;

    private routeDataSubscription: any;
    public predicate: any;
    public reverse: any;
    public filters: ComputationalThreadFilter;
    private searchSubscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private permissionService: PermissionService,
        private computationalThreadService: ComputationalThreadService,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private datePipe: DatePipe,
        private eventManager: JhiEventManager,
        private translateService: TranslateService
    ) {
        this.routeDataSubscription = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
            this.itemsPerPage = data['pagingParams'].itemsPerPage;
        });

        this.filters = new ComputationalThreadFilter(this.datePipe);
    }

    ngOnInit() {
        this.activatedRoute.queryParams.subscribe((params) => {
            this.filters.fromQueryParams(params).subscribe(() => this.loadAll());
        });

        this.registerChangesInSearchComputationalThreads();
        this.registerChangesInComputationalThreads();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
        this.eventManager.destroy(this.searchSubscription);
    }

    private loadAll() {
        this.computationalThreadService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                query: this.filters ? this.filters.toQuery() : '',
                includeDeleted: this.filters ? this.filters.includeDeleted : false,
                lastExecution:
                    this.filters && this.filters.lastExecution ? this.filters.lastExecution : '',
                lastExecutionByResult:
                    this.filters && this.filters.lastExecutionByResult
                        ? this.filters.lastExecutionByResult
                        : ''
            })
            .subscribe((res: ResponseWrapper) => this.onSuccess(res.json, res.headers));
    }

    private onSuccess(data: ComputationalThreadsBase[], headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.computationalThreadList = data;
    }

    private registerChangesInComputationalThreads() {
        this.eventSubscriber = this.eventManager.subscribe(
            ComputationalThreadFormComponent.EVENT_NAME,
            (response) => this.loadAll()
        );
    }

    // VALIDATIONS
    public canCreateComputationalThread(): boolean {
        return this.permissionService.canManageComputationalThread();
    }

    // FILTERS
    public sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    public transition() {
        this.router.navigate(['/computational-threads'], {
            queryParams: Object.assign({}, this.activatedRoute.snapshot.queryParams, {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            })
        });
    }

    private registerChangesInSearchComputationalThreads() {
        this.searchSubscription = this.eventManager.subscribe(
            ComputationalThreadSearchComponent.EVENT_NAME,
            () => {
                this.page = 1;
                const queryParams = Object.assign(
                    {},
                    this.filters.toUrl(this.activatedRoute.snapshot.queryParams),
                    { page: this.page }
                );

                this.router.navigate(['computational-threads'], { queryParams });
            }
        );
    }

    public getResultBadgeClass(execution: string): any {
        if (execution) {
            return {
                'badge-success': execution === Result.SUCCESS,
                'badge-danger': execution === Result.FAILED,
                'badge-warning': execution === Result.WAITING,
                'badge-primary': execution === Result.RUNNING,
                'badge-default': execution === Result.DUPLICATED
            };
        }
        return '';
    }

    getResultName(execution: string): string {
        return execution
            ? this.translateService.instant(`computationalThread.execution.result.${execution}`)
            : '';
    }
}
