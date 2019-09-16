import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { EtlService } from '../etl.service';
import { Parameter } from '../../parameter';
import { EtlParameterListComponent } from './etl-parameter-list.component';

@Component({
    selector: 'ac-etl-parameter-delete-dialog',
    templateUrl: 'etl-parameter-delete-dialog.component.html'
})
export class EtlParameterDeleteDialogComponent implements OnInit {
    public parameter: Parameter;

    constructor(
        private etlService: EtlService,
        private activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {}

    public delete() {
        this.etlService
            .deleteParameter(this.parameter.etlId, this.parameter.id)
            .subscribe((parameter) => {
                this.eventManager.broadcast({
                    name: EtlParameterListComponent.EVENT_NAME,
                    content: parameter
                });
                this.activeModal.close('deleted');
            });
    }

    public clear() {
        this.activeModal.dismiss('closed');
    }
}
