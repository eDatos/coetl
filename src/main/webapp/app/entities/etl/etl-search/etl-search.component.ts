import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';
import { Subject, Subscription } from 'rxjs';

import { EtlFilter } from './etl-filter.model';
import { Type } from '../etl.model';

@Component({
    selector: 'ac-etl-search',
    templateUrl: 'etl-search.component.html'
})
export class EtlSearchComponent implements OnInit, OnDestroy {
    @Input() filters: EtlFilter;
    susbcription: Subscription;
    typeEnum = Type;

    private filterChangesSubject: Subject<any>;

    constructor(private eventManager: JhiEventManager) {
        this.filterChangesSubject = new Subject<any>();
    }

    ngOnInit() {
        this.susbcription = this.filterChangesSubject
            .debounceTime(300)
            .subscribe(() =>
                this.eventManager.broadcast({ name: 'etlSearch', content: this.filters })
            );
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.susbcription);
    }

    filter() {
        this.filterChangesSubject.next();
    }

    resetFilters() {
        this.filters.reset();
        this.filter();
    }
}
