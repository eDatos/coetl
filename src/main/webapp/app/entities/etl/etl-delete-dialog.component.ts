import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Etl } from './etl.model';
import { EtlService } from './etl.service';
import { EtlFormComponent } from './etl-form.component';

@Component({
    selector: 'ac-etl-delete-dialog',
    templateUrl: 'etl-delete-dialog.component.html'
})
export class EtlDeleteDialogComponent implements OnInit {
    etl: Etl;

    constructor(
        private etlService: EtlService,
        private activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {}

    clear() {
        this.activeModal.dismiss(false);
    }

    confirmDelete(idEtl: number) {
        this.etlService.delete(idEtl).subscribe((response) => {
            this.eventManager.broadcast({
                name: EtlFormComponent.EVENT_NAME,
                content: response
            });
            this.activeModal.close(true);
        });
    }
}
