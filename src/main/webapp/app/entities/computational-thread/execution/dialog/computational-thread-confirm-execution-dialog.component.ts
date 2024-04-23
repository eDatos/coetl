import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { Observable } from 'rxjs';
import { ComputationalThreads } from '../../computational-thread.model';
import {
    ComputationalThreadExecution,
    Result,
    Type
} from '../../computational-thread-execution.model';
import { ComputationalThreadService } from '../../computational-thread.service';
import { ComputationalThreadExecutionListComponent } from '../computational-thread-execution-list.component';

@Component({
    selector: 'ac-computational-thread-confirm-execution-dialog',
    templateUrl: 'computational-thread-confirm-execution-dialog.component.html'
})
export class ComputationalThreadConfirmExecutionDialogComponent implements OnInit {
    public thread: ComputationalThreads;
    public requestSended: boolean;

    constructor(
        private activeModal: NgbActiveModal,
        private computationalThreadService: ComputationalThreadService,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.requestSended = false;
    }

    confirmExecution(idThread: number) {
        this.requestSended = true;
        const etlEditObservable = this.computationalThreadService.createExecution(
            idThread,
            this.initializeExecutionDTO(idThread)
        );
        this.subscribeToPreExecuteResponse(etlEditObservable);
    }

    private initializeExecutionDTO(idThread: number) {
        const newExecution = new ComputationalThreadExecution();
        newExecution.idThread = idThread;
        newExecution.type = Type.MANUAL;
        newExecution.result = Result.RUNNING;
        return newExecution;
    }

    private subscribeToPreExecuteResponse(result: Observable<ComputationalThreadExecution>) {
        result.subscribe((result) => this.onPreSaveSuccess(result));
    }

    private onPreSaveSuccess(threadExecution: ComputationalThreadExecution) {
        this.eventManager.broadcast({
            name: ComputationalThreadExecutionListComponent.EVENT_NAME,
            content: 'executed'
        });
        this.activeModal.close(true);
    }

    clear() {
        this.activeModal.dismiss(false);
    }
}
