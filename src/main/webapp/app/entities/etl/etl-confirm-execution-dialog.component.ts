import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Etl } from './etl.model';
import { EtlService } from './etl.service';
import { EtlExecutionListComponent } from './etl-execution-list/etl-execution-list.component';

@Component({
    selector: 'ac-etl-confirm-execution-dialog',
    templateUrl: 'etl-confirm-execution-dialog.component.html'
})
export class EtlConfirmExecutionDialogComponent implements OnInit {
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

    confirmExecution(idEtl: number) {
        this.etlService.execute(idEtl).subscribe(() => {
            this.eventManager.broadcast({
                name: EtlExecutionListComponent.EVENT_NAME,
                content: 'executed'
            });
            this.activeModal.close(true);
        });
    }
}
