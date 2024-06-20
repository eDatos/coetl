import { Component, OnInit, OnDestroy, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import { GenericModalService, HasTitlesContainer, PermissionService } from '../../../shared';
import { ActivatedRoute, Router } from '@angular/router';
import { ComputationalThreads } from '../computational-thread.model';
import { Observable, Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { Autosize } from 'ng-autosize';
import { ComputationalThreadService } from '../computational-thread.service';
import { ExternalItem, ExternalItemService } from '../../external-item';
import { Etl } from '../../etl/etl.model';
import { ComputationalThreadsEtl } from '../computational-threads-etl-model';
import { ComputationalThreadConfirmExecutionDialogComponent } from '../execution/dialog/computational-thread-confirm-execution-dialog.component';
import { ComputationalThreadDeleteDialogComponent } from '../delete-dialog/computational-thread-delete-dialog.component';
import { ComputationalThreadRestoreDialogComponent } from '../restore-dialog/computational-thread-restore-dialog.component';
import { EtlExpressionHelpDialogComponent } from '../../etl/etl-expression-help-dialog/etl-expression-help-dialog.component';

@Component({
    selector: 'ac-computational-thread-form',
    templateUrl: 'computational-thread-form.component.html'
})
export class ComputationalThreadFormComponent
    implements OnInit, AfterViewInit, OnDestroy, HasTitlesContainer {
    public static EVENT_NAME = 'computationalThreadListModification';

    public instance: ComputationalThreadFormComponent;
    public computationalThreads: ComputationalThreads;
    public isSaving: boolean;
    private updatesSubscription: Subscription;
    public externalItemsSuggestions: ExternalItem[] = [];
    public selectedEtls: Etl[];
    public etlList: Etl[];

    // VISTAS
    @ViewChild('titlesContainer') titlesContaner: ElementRef;
    @ViewChild(Autosize) executionDescriptionContainer: Autosize;
    @ViewChild(Autosize) descriptionContainer: Autosize;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private permissionService: PermissionService,
        private computationalThreadService: ComputationalThreadService,
        private externalItemService: ExternalItemService,
        private genericModalService: GenericModalService
    ) {
        this.instance = this;
        this.computationalThreads = new ComputationalThreads();
    }

    ngOnInit() {
        this.selectedEtls = [];
        this.etlList = [];
        this.isSaving = false;
        this.computationalThreads = !!this.route.snapshot.data['computationalThread']
            ? this.route.snapshot.data['computationalThread']
            : new ComputationalThreads();
        this.registerChangesOnEtl();
    }

    ngOnDestroy() {}

    // EVENTOS
    public save() {
        this.etlBaseToComputationalThreadEtl();
        this.isSaving = true;
        const etlEditObservable = !!this.computationalThreads.id
            ? this.computationalThreadService.update(this.computationalThreads)
            : this.computationalThreadService.create(this.computationalThreads);
        this.subscribeToSaveResponse(etlEditObservable);
    }

    private subscribeToSaveResponse(result: Observable<ComputationalThreads>) {
        result.subscribe(
            (res: ComputationalThreads) => this.onSaveSuccess(res),
            () => this.onSaveError()
        );
    }

    private onSaveSuccess(result: ComputationalThreads) {
        this.isSaving = false;
        this.eventManager.broadcast({
            name: ComputationalThreadFormComponent.EVENT_NAME,
            content: 'saved'
        });
        this.router.navigate(['computational-threads', result.id]);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    public cancel() {
        if (this.computationalThreads.id) {
            this.router.navigate(['/computational-threads', this.computationalThreads.id]);
        } else {
            this.router.navigate(['/computational-threads']);
        }
    }

    public delete() {
        const copy = Object.assign(new ComputationalThreads(), this.computationalThreads);
        this.genericModalService.open(
            <any>ComputationalThreadDeleteDialogComponent,
            { thread: copy },
            { container: '.app' }
        );
    }

    public restore() {
        const copy = Object.assign(new ComputationalThreads(), this.computationalThreads);
        this.genericModalService.open(
            <any>ComputationalThreadRestoreDialogComponent,
            { thread: copy },
            { container: '.app' }
        );
    }

    public execute() {
        const copy = Object.assign(new ComputationalThreads(), this.computationalThreads);
        this.genericModalService.open(
            <any>ComputationalThreadConfirmExecutionDialogComponent,
            { thread: copy },
            { container: '.app' }
        );
    }

    private registerChangesOnEtl() {
        this.updatesSubscription = this.eventManager.subscribe(
            ComputationalThreadFormComponent.EVENT_NAME,
            (result) => {
                if (result.content !== 'saved') {
                    this.load(result.content);
                }
            }
        );
    }

    private load(entity: any) {
        this.computationalThreads = Object.assign(new ComputationalThreads(), entity);
    }

    public completeMethodStatisticalOperations(event) {
        this.externalItemService
            .findAll({
                query: event.query
            })
            .map((res) => res.json)
            .subscribe((operaciones) => (this.externalItemsSuggestions = operaciones));
    }

    public help() {
        this.genericModalService.open(
            <any>EtlExpressionHelpDialogComponent,
            {},
            { container: '.app' }
        );
    }

    public canShowNextExecution(): boolean {
        return this.computationalThreads.isPlanning() && !!this.computationalThreads.id;
    }

    // VERIFICACIONES
    public isEditMode(): Boolean {
        const lastPath = this.route.snapshot.url[this.route.snapshot.url.length - 1].path;
        return lastPath === 'edit' || lastPath === 'computational-threads-new';
    }

    public canEdit(): boolean {
        return this.permissionService.canManageComputationalThread(
            this.computationalThreads.externalItem
        );
    }

    public canSave(): boolean {
        return !this.isSaving;
    }

    // VIEW
    public getTitlesContainer(): ElementRef {
        return this.titlesContaner;
    }

    ngAfterViewInit() {
        setTimeout(() => this.adjustAllContainers(), null);
    }

    private adjustAllContainers() {
        this.executionDescriptionContainer.adjust();
        this.descriptionContainer.adjust();
    }

    // CHILD ETLS
    private etlBaseToComputationalThreadEtl() {
        const tmpEtlThread = [];
        this.selectedEtls.forEach((selectedEtl, index) => {
            tmpEtlThread.push(
                this.setComputationalThreadEtl(
                    selectedEtl,
                    this.computationalThreads.computationalThreadsEtl.find(
                        (etlThread) => etlThread.etl.id == selectedEtl.id
                    ),
                    index
                )
            );
        });
        this.computationalThreads.computationalThreadsEtl = [];
        this.computationalThreads.computationalThreadsEtl = tmpEtlThread;
    }

    private setComputationalThreadEtl(etlBase: Etl, etlThreadExistente: any, order: number) {
        if (etlThreadExistente && etlThreadExistente.id !== null) {
            etlThreadExistente.executionOrder = order;
            return etlThreadExistente;
        }
        const tmpThread: ComputationalThreadsEtl = new ComputationalThreadsEtl();
        tmpThread.etl = etlBase;
        tmpThread.executionOrder = order;
        return tmpThread;
    }
}
