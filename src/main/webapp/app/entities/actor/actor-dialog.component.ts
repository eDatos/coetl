import { Component, OnDestroy, OnInit } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Observable } from 'rxjs/Rx';

import { ResponseWrapper } from '../../shared';
import { Pelicula, PeliculaFilter, PeliculaService } from '../pelicula';
import { ActorPopupService } from './actor-popup.service';
import { Actor } from './actor.model';
import { ActorService } from './actor.service';

@Component({
    selector: 'jhi-actor-dialog',
    templateUrl: './actor-dialog.component.html'
})
export class ActorDialogComponent implements OnInit {

    actor: Actor;
    isSaving: boolean;
    isDeleting: boolean;

    peliculas: Pelicula[];
    peliculaFilter: PeliculaFilter;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private actorService: ActorService,
        private peliculaService: PeliculaService,
        private eventManager: JhiEventManager
    ) {
        this.peliculaFilter = new PeliculaFilter();
    }

    ngOnInit() {
        this.isSaving = false;
        this.isDeleting = false;
        if (this.actor.id !== undefined) {
            this.peliculaFilter.actores = Array.of<Actor>(this.actor);
            this.peliculaService.query({ query: this.peliculaFilter.toQuery() })
                .subscribe((res: ResponseWrapper) => { this.peliculas = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        }
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.actor.id !== undefined) {
            this.subscribeToSaveResponse(
                this.actorService.update(this.actor));
        } else {
            this.subscribeToSaveResponse(
                this.actorService.create(this.actor));
        }
    }

    private subscribeToSaveResponse(result: Observable<Actor>) {
        result.subscribe((res: Actor) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Actor) {
        this.eventManager.broadcast({ name: 'actorListModification', content: 'Actor saved'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackPeliculaById(index: number, item: Pelicula) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }

    public toggleIsDeleting() {
        this.isDeleting = !this.isDeleting;
    }

    public confirmDelete(id: number) {
        this.actorService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({name: 'actorListModification', content: 'Actor deleted'});
            this.activeModal.dismiss(true);
        },
        (error) => this.onDeleteError(error));
    }

    private onDeleteError(error): void {
        this.isDeleting = false;
        this.onError(error);
    }
}

@Component({
    selector: 'jhi-actor-popup',
    template: ''
})
export class ActorPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private actorPopupService: ActorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.actorPopupService
                    .open(ActorDialogComponent as Component, params['id']);
            } else {
                this.actorPopupService
                    .open(ActorDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
