import { Component, OnInit, OnDestroy, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import { HasTitlesContainer, PermissionService } from '../../../shared';
import { ActivatedRoute, Router } from '@angular/router';
import { ComputationalThreads } from '../computational-thread.model';
import { Observable, Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { Autosize } from 'ng-autosize';
import { ComputationalThreadService } from '../computational-thread.service';
import { ExternalItem, ExternalItemService } from '../../external-item';

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

    // VISTAS
    @ViewChild('titlesContainer') titlesContaner: ElementRef;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private permissionService: PermissionService,
        private computationalThreadService: ComputationalThreadService,
        private externalItemService: ExternalItemService
    ) {
        this.instance = this;
    }

    ngOnInit() {
        this.isSaving = false;
        this.computationalThreads = !!this.route.snapshot.data['computationalThread']
            ? this.route.snapshot.data['computationalThread']
            : new ComputationalThreads();
        this.registerChangesOnEtl();
    }

    ngOnDestroy() {}

    // EVENTOS
    public save() {
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

    public clear() {
        if (this.computationalThreads.id) {
            this.router.navigate(['/computational-threads', this.computationalThreads.id]);
        } else {
            this.router.navigate(['/computational-threads']);
        }
    }

    public delete() {
        const copy = Object.assign(new ComputationalThreads(), this.computationalThreads);
    }

    public restore() {
        const copy = Object.assign(new ComputationalThreads(), this.computationalThreads);
    }

    public execute() {
        const copy = Object.assign(new ComputationalThreads(), this.computationalThreads);
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

    private adjustAllContainers() {}
}
