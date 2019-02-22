import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'ac-etl-expression-help-dialog',
    templateUrl: 'etl-expression-help-dialog.component.html'
})
export class EtlExpressionHelpDialogComponent implements OnInit {
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
