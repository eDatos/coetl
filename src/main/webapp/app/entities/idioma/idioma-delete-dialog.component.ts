import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Idioma } from './idioma.model';
import { IdiomaPopupService } from './idioma-popup.service';
import { IdiomaService } from './idioma.service';

@Component({
    selector: 'jhi-idioma-delete-dialog',
    templateUrl: './idioma-delete-dialog.component.html'
})
export class IdiomaDeleteDialogComponent {

    idioma: Idioma;

    constructor(
        private idiomaService: IdiomaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.idiomaService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'idiomaListModification',
                content: 'Deleted an idioma'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-idioma-delete-popup',
    template: ''
})
export class IdiomaDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private idiomaPopupService: IdiomaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.idiomaPopupService
                .open(IdiomaDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
