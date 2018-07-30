import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Etl } from './etl.model';
import { EtlService } from './etl.service';
import { EtlFormComponent } from './etl-form.component';

@Component({
    selector: 'ac-etl-restore-dialog',
    templateUrl: 'etl-restore-dialog.component.html'
})
export class EtlRestoreDialogComponent implements OnInit {
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

    confirmRestore(idEtl: number) {
        this.etlService.restore(idEtl).subscribe((response) => {
            this.eventManager.broadcast({
                name: EtlFormComponent.EVENT_NAME,
                content: response
            });
            this.activeModal.close(true);
        });
    }
}
