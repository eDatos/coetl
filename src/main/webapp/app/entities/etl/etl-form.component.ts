import { Component, OnInit, AfterViewInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import { Autosize } from 'ng-autosize';
import { Subscription, Observable } from 'rxjs';

import { GenericModalService, PermissionService, HasTitlesContainer } from '../../shared';
import { Etl, Type } from './etl.model';
import { EtlService } from './etl.service';
import { EtlDeleteDialogComponent } from './etl-delete-dialog.component';
import { EtlRestoreDialogComponent } from './etl-restore-dialog.component';
import { File } from '../documento/file.model';
import { FileService } from '../documento';
import { Fieldset } from '../../../../../../node_modules/primeng/primeng';

@Component({
    selector: 'ac-etl-form',
    templateUrl: 'etl-form.component.html'
})
export class EtlFormComponent implements OnInit, AfterViewInit, OnDestroy, HasTitlesContainer {
    public static EVENT_NAME = 'etlListModification';

    instance: EtlFormComponent;

    etl: Etl;
    typeEnum = Type;
    isSaving: boolean;

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
        private fileService: FileService
    ) {
        this.instance = this;
        this.fileResourceUrl = 'api/files';
    }

    ngOnInit() {
        this.isSaving = false;
        if (this.route.snapshot.data['etl']) {
            this.etl = this.route.snapshot.data['etl'];
        } else {
            this.etl = new Etl();
        }
        // this.etl = this.route.snapshot.data['etl'] ? this.route.snapshot.data['etl'] : new Etl();
        this.registerChangesOnEtl();
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
        this.isSaving = true;
        if (this.etl.id !== undefined) {
            this.subscribeToSaveResponse(this.etlService.update(this.etl));
        } else {
            this.subscribeToSaveResponse(this.etlService.create(this.etl));
        }
    }

    delete() {
        const copy = Object.assign(new Etl(), this.etl);
        this.genericModalService.open(<any>EtlDeleteDialogComponent, { etl: copy });
    }

    restore() {
        const copy = Object.assign(new Etl(), this.etl);
        this.genericModalService.open(<any>EtlRestoreDialogComponent, { etl: copy });
    }

    isEditMode(): Boolean {
        const lastPath = this.route.snapshot.url[this.route.snapshot.url.length - 1].path;
        return lastPath === 'edit' || lastPath === 'etl-new';
    }

    canEdit(): boolean {
        return this.permissionService.canManageEtl();
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

    onCodeFileUpload(event) {
        const codeFile = JSON.parse(event.xhr.response);
        this.etl.etlFile = codeFile;
    }

    deleteCodeFile(file: File) {
        this.fileService.delete(file.id).subscribe(() => (this.etl.etlFile = undefined));
    }

    onDescriptionFileUpload(event) {
        const descriptionFile = JSON.parse(event.xhr.response);
        this.etl.descriptionFile = descriptionFile;
    }

    deleteDescriptionFile(file: File) {
        this.fileService.delete(file.id).subscribe(() => (this.etl.descriptionFile = undefined));
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
}
