import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { ComputationalThreads } from '../computational-thread.model';
import { ComputationalThreadService } from '../computational-thread.service';
import { ComputationalThreadFormComponent } from '../form/computational-thread-form.component';

@Component({
    selector: 'ac-computational-thread-restore-dialog',
    templateUrl: 'computational-thread-restore-dialog.component.html'
})
export class ComputationalThreadRestoreDialogComponent implements OnInit {
    public thread: ComputationalThreads;

    constructor(
        private computationalThreadService: ComputationalThreadService,
        private activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {}

    public clear() {
        this.activeModal.dismiss(false);
    }

    public confirmRestore(idThread: number) {
        this.computationalThreadService.restore(idThread).subscribe((response) => {
            this.eventManager.broadcast({
                name: ComputationalThreadFormComponent.EVENT_NAME,
                content: response
            });
            this.activeModal.close(true);
        });
    }
}
