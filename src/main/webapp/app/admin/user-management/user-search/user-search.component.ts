import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { JhiLanguageService, JhiEventManager } from 'ng-jhipster';

import { Subject, Subscription } from 'rxjs';
import { User } from '../../../shared/index';
import { UserFilter } from './index';

@Component({
    selector: 'ac-user-search',
    templateUrl: 'user-search.component.html'
})

export class UserSearchComponent implements OnInit, OnDestroy {

    private filterChangesSubject: Subject<any> = new Subject<any>();
    subscription: Subscription;

    @Input()
    filters: UserFilter;

    constructor(
        private eventManager: JhiEventManager,
    ) {
        this.filters = new UserFilter();
    }

    ngOnInit() {
        this.subscription = this.filterChangesSubject
            .debounceTime(300)
            .subscribe(() =>
                this.eventManager.broadcast({
                    name: 'userSearch',
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
