import { Component, OnInit, AfterViewInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import { Autosize } from 'ng-autosize';
import { Subscription, Observable } from 'rxjs';

import {
    GenericModalService,
    PermissionService,
    HasTitlesContainer,
    AcAlertService,
    ResponseWrapper
} from '../../shared';
import { Etl, PentahoType, HopType, Type, ExecutionPlatform } from './etl.model';
import { EtlService } from './etl.service';
import { EtlDeleteDialogComponent } from './etl-delete-dialog.component';
import { EtlRestoreDialogComponent } from './etl-restore-dialog.component';
import { EtlConfirmExecutionDialogComponent } from './etl-confirm-execution-dialog.component';
import { EtlExpressionHelpDialogComponent } from './etl-expression-help-dialog/etl-expression-help-dialog.component';
import { ExternalItem, ExternalItemService } from '../external-item';
import { ComputationalThreadsBase } from '../computational-thread/computational-thread.model';

@Component({
    selector: 'ac-etl-form',
    templateUrl: 'etl-form.component.html'
})
export class EtlFormComponent implements OnInit, AfterViewInit, OnDestroy, HasTitlesContainer {
    public static EVENT_NAME = 'etlListModification';

    instance: EtlFormComponent;

    externalItemsSuggestions: ExternalItem[] = [];

    etl: Etl;
    typeEnum = Type;
    executionPlatformEnum = ExecutionPlatform;
    isSaving: boolean;
    public threads: ComputationalThreadsBase[];
    private previousExecutionPlatform: string;

    updatesSubscription: Subscription;

    fileResourceUrl: string;

    @ViewChild(Autosize) purposeContainer: Autosize;

    @ViewChild(Autosize) organizationInChargeContainer: Autosize;

    @ViewChild(Autosize) functionalInChargeContainer: Autosize;

    @ViewChild(Autosize) technicalInChargeContainer: Autosize;

    @ViewChild(Autosize) commentsContainer: Autosize;

    @ViewChild(Autosize) executionDescriptionContainer: Autosize;

    @ViewChild('titlesContainer') titlesContaner: ElementRef;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private etlService: EtlService,
        private genericModalService: GenericModalService,
        private eventManager: JhiEventManager,
        private permissionService: PermissionService,
        private translateService: TranslateService,
        private externalItemService: ExternalItemService,
        public acAlertService: AcAlertService
    ) {
        this.instance = this;
        this.fileResourceUrl = 'api/files';
    }

    ngOnInit() {
        this.isSaving = false;
        this.etl = !!this.route.snapshot.data['etl'] ? this.route.snapshot.data['etl'] : new Etl();
        this.threads = [];
        this.registerChangesOnEtl();
        this.previousExecutionPlatform = this.etl.id ? this.etl.executionPlatform : '';
        if (this.etl.id) {
            this.loadAllThreads();
        }
    }

    ngAfterViewInit() {
        setTimeout(() => this.adjustAllContainers(), null);
    }

    ngOnDestroy() {}

    clear() {
        if (this.etl.id) {
            this.router.navigate(['/etl', this.etl.id]);
        } else {
            this.router.navigate(['/etl']);
        }
    }

    save() {
        if (this.validateIfCanEditExecutionPlatform()) {
            this.isSaving = true;
            const etlEditObservable = !!this.etl.id
                ? this.etlService.update(this.etl)
                : this.etlService.create(this.etl);
            this.subscribeToSaveResponse(etlEditObservable);
        } else {
            this.acAlertService.error(
                this.getTranslationName(`error.etl.canNotEditExecutionPlatform`)
            );
        }
    }

    delete() {
        const copy = Object.assign(new Etl(), this.etl);
        this.genericModalService.open(
            <any>EtlDeleteDialogComponent,
            { etl: copy },
            { container: '.app' }
        );
    }

    public edit() {
        this.etlService.existExecutionRunningOrWaiting(this.etl.id).subscribe((exist) => {
            if (!exist) {
                this.router.navigate(['/etl', this.etl.id, 'edit']);
            } else {
                this.acAlertService.error(this.getTranslationName(`error.etl.canNotEdit`));
            }
        });
    }

    restore() {
        const copy = Object.assign(new Etl(), this.etl);
        this.genericModalService.open(
            <any>EtlRestoreDialogComponent,
            { etl: copy },
            { container: '.app' }
        );
    }

    execute() {
        const copy = Object.assign(new Etl(), this.etl);
        this.genericModalService.open(
            <any>EtlConfirmExecutionDialogComponent,
            { etl: copy },
            { container: '.app' }
        );
    }

    help() {
        this.genericModalService.open(
            <any>EtlExpressionHelpDialogComponent,
            {},
            { container: '.app' }
        );
    }

    isEditMode(): Boolean {
        const lastPath = this.route.snapshot.url[this.route.snapshot.url.length - 1].path;
        return lastPath === 'edit' || lastPath === 'etl-new';
    }

    canEdit(): boolean {
        return this.permissionService.canManageEtl(this.etl.externalItem);
    }

    private getTranslationName(jsonToTranslate: string): string {
        return this.translateService.instant(jsonToTranslate);
    }

    getDeletedMessage(etl: Etl): string {
        const codeMessage = etl.isDeleted()
            ? 'coetlApp.etl.state.isDeleted'
            : 'coetlApp.etl.state.isNotDeleted';
        return this.translateService.instant(codeMessage);
    }

    getTitlesContainer(): ElementRef {
        return this.titlesContaner;
    }

    canShowNextExecution(): boolean {
        return this.etl.isPlanning() && !!this.etl.id;
    }

    canSave(): boolean {
        return !this.isSaving && !!this.etl.uriRepository;
    }

    private subscribeToSaveResponse(result: Observable<Etl>) {
        result.subscribe((res: Etl) => this.onSaveSuccess(res), () => this.onSaveError());
    }

    private onSaveSuccess(result: Etl) {
        this.isSaving = false;
        this.eventManager.broadcast({ name: EtlFormComponent.EVENT_NAME, content: 'saved' });
        this.router.navigate(['etl', result.id]);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private registerChangesOnEtl() {
        this.updatesSubscription = this.eventManager.subscribe(
            EtlFormComponent.EVENT_NAME,
            (result) => {
                if (result.content !== 'saved') {
                    this.load(result.content);
                }
            }
        );
    }

    private load(entity: any) {
        this.etl = Object.assign(new Etl(), entity);
    }

    private adjustAllContainers() {
        this.purposeContainer.adjust();
        this.organizationInChargeContainer.adjust();
        this.functionalInChargeContainer.adjust();
        this.technicalInChargeContainer.adjust();
        this.commentsContainer.adjust();
        this.executionDescriptionContainer.adjust();
    }

    completeMethodStatisticalOperations(event) {
        this.externalItemService
            .findAll({
                query: event.query
            })
            .map((res) => res.json)
            .subscribe((operaciones) => (this.externalItemsSuggestions = operaciones));
    }

    suggestionType() {
        if (this.etl.executionPlatform == ExecutionPlatform.PENTAHO) {
            return PentahoType;
        } else if (this.etl.executionPlatform == ExecutionPlatform.APACHE_HOP) {
            return HopType;
        }
        return {};
    }

    updateType(event) {
        this.etl.type = undefined;
    }

    private loadAllThreads() {
        this.etlService.findThreadsByEtl(this.etl.id).subscribe((result: ResponseWrapper) => {
            this.threads = result.json;
        });
    }

    private validateIfCanEditExecutionPlatform(): boolean {
        if (!!this.etl.id) {
            this.loadAllThreads();
            if (
                this.threads.length > 0 &&
                this.previousExecutionPlatform != '' &&
                this.etl.executionPlatform != this.executionPlatformEnum.APACHE_HOP
            ) {
                return false;
            }
        }
        return true;
    }
}
