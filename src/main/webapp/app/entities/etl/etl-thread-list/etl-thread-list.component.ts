import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ComputationalThreadsBase } from '../../computational-thread/computational-thread.model';

@Component({
    selector: 'ac-etl-thread-list',
    templateUrl: 'etl-thread-list.component.html',
    styleUrls: ['etl-thread-list.component.scss']
})
export class EtlThreadListComponent implements OnInit, OnDestroy {
    public static EVENT_NAME = 'EtlThreadList';

    @Input() threads: ComputationalThreadsBase[];
    @Input() isEdit: boolean;

    ngOnInit() {}

    ngOnDestroy() {}

    public hasThreads() {
        return !this.threads || this.threads.length === 0;
    }
}
