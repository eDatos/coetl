import { Component, OnInit, OnDestroy, Input, OnChanges } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';

import { Subject, Subscription } from 'rxjs';
import { User, Rol, RolService } from '../../../shared/index';
import { UserFilter } from './index';

@Component({
    selector: 'ac-user-search',
    templateUrl: 'user-search.component.html'
})

export class UserSearchComponent implements OnInit, OnDestroy, OnChanges {

    private filterChangesSubject: Subject<any> = new Subject<any>();
    subscription: Subscription;

    @Input()
    roles: Rol[];

    @Input()
    filters: UserFilter;

    constructor(
        private eventManager: JhiEventManager,
        private rolService: RolService
    ) {
        this.filters = new UserFilter();
    }

    ngOnInit() {
        this.subscription = this.filterChangesSubject
            .debounceTime(300)
            .subscribe(() =>
                this.eventManager.broadcast({
                    name: 'userSearch',
                    content: this.filtersToUrl(),
                })
            );
    }

    ngOnChanges() {
        this.filters.setAllRoles(this.roles);
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

    rolItemTemplate(item: Rol) {
        return item.nombre;
    }

    completeMethodRoles(event) {
        this.rolService.query({ query: `NOMBRE ILIKE '%${event.query}%'` })
            .subscribe((result) => this.roles = result.json);
    }

    private filtersToUrl() {
        return this.filters.toUrl();
    }
}
