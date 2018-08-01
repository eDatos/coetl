import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';

import { Subject, Subscription } from 'rxjs';
import { UserFilter } from '.';
import { Rol } from '../../../shared';

@Component({
    selector: 'ac-user-search',
    templateUrl: 'user-search.component.html'
})
export class UserSearchComponent implements OnInit, OnDestroy {
    private filterChangesSubject: Subject<any> = new Subject<any>();
    subscription: Subscription;
    roleEnum = Rol;

    @Input() filters: UserFilter;

    constructor(private eventManager: JhiEventManager) {
        this.filters = new UserFilter();
    }

    ngOnInit() {
        this.subscription = this.filterChangesSubject.debounceTime(300).subscribe(() =>
            this.eventManager.broadcast({
                name: 'userSearch',
                content: this.filtersToUrl()
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

    private filtersToUrl() {
        return this.filters.toUrl();
    }
}
