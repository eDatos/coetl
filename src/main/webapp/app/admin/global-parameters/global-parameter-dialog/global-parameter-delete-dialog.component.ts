import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { Parameter } from '../../../entities/parameter';
import { GlobalParameterComponent } from '../global-parameter.component';
import { GloablParameterService } from '../global-parameter.service';

@Component({
    selector: 'ac-global-parameter-delete-dialog',
    templateUrl: 'global-parameter-delete-dialog.component.html'
})
export class GlobalParameterDeleteDialogComponent implements OnInit {
    public parameter: Parameter;

    constructor(
        private globalParameterService: GloablParameterService,
        private activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {}

    public delete() {
        this.globalParameterService.deleteParameter(this.parameter.id).subscribe((parameter) => {
            this.eventManager.broadcast({
                name: GlobalParameterComponent.EVENT_NAME,
                content: parameter
            });
            this.activeModal.close('deleted');
        });
    }

    public clear() {
        this.activeModal.dismiss('closed');
    }
}
