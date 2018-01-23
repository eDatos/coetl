import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Actor } from './actor.model';
import { ActorPopupService } from './actor-popup.service';
import { ActorService } from './actor.service';
import { Pelicula, PeliculaService } from '../pelicula';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-actor-dialog',
    templateUrl: './actor-dialog.component.html'
})
export class ActorDialogComponent implements OnInit {

    actor: Actor;
    isSaving: boolean;

    peliculas: Pelicula[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private actorService: ActorService,
        private peliculaService: PeliculaService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.peliculaService.query()
            .subscribe((res: ResponseWrapper) => { this.peliculas = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
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
        this.eventManager.broadcast({ name: 'actorListModification', content: 'OK'});
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
