import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { JhiLanguageService, JhiEventManager } from 'ng-jhipster';

import { Subject, Subscription } from 'rxjs';
import { EntityFilter } from '../../../shared/index';
import { OperacionFilter } from './index';
import { Operacion } from '../index';

@Component({
    selector: 'ac-operacion-search',
    templateUrl: 'operacion-search.component.html',
})

export class OperacionSearchComponent implements OnInit, OnDestroy {

    private filterChangesSubject: Subject<any> = new Subject<any>();
    subscription: Subscription;

    @Input()
    filters: OperacionFilter;

    constructor(
        private eventManager: JhiEventManager,
    ) {
    }

    ngOnInit() {
        this.subscription = this.filterChangesSubject
            .debounceTime(300)
            .subscribe(() =>
                this.eventManager.broadcast({
                    name: 'operacionSearch',
                    content: this.filters
                })
            );
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

    filter() {
        this.filterChangesSubject.next();
    }

    resetFilters() {
        this.filters.reset();
        this.filter();
    }
}
