import { Component, OnInit, OnDestroy } from '@angular/core';

@Component({
    selector: 'computational-thread',
    templateUrl: './computational-thread.component.html'
})
export class ComputationalThreadComponent implements OnInit, OnDestroy {
    public static EVENT_NAME = 'computationalThreadEvent';

    constructor() {}

    ngOnInit() {}

    ngOnDestroy() {}
}
