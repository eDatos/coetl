import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'ac-etl-parameter-help-dialog',
    templateUrl: 'etl-parameter-help-dialog.component.html'
})
export class EtlParameterHelpDialogComponent implements OnInit {
    constructor(private activeModal: NgbActiveModal) {}

    ngOnInit() {}

    clear() {
        this.closeModal();
    }

    accept() {
        this.closeModal();
    }

    private closeModal() {
        this.activeModal.dismiss(false);
    }
}
