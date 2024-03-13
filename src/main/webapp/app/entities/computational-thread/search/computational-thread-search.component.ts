import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { ComputationalThreadFilter } from './computational-thread-search-filter.model';
import { JhiEventManager } from 'ng-jhipster';
import { Subject, Subscription } from 'rxjs';

@Component({
    selector: 'ac-computational-thread-search',
    templateUrl: 'computational-thread-search.component.html'
})
export class ComputationalThreadSearchComponent implements OnInit, OnDestroy {
    public static EVENT_NAME = 'ComputationalThreadSearch';

    @Input() filters: ComputationalThreadFilter;

    private filterChangesSubject: Subject<any>;
    public options: any;
    private susbcription: Subscription;

    constructor(private eventManager: JhiEventManager, private translateService: TranslateService) {
        this.filterChangesSubject = new Subject<any>();
        this.options = [
            { label: this.translateService.instant('global.yes'), value: 'true' },
            { label: this.translateService.instant('global.no'), value: 'false' }
        ];
    }

    ngOnInit() {
        this.susbcription = this.filterChangesSubject
            .debounceTime(300)
            .subscribe(() =>
                this.eventManager.broadcast({
                    name: ComputationalThreadSearchComponent.EVENT_NAME,
                    content: this.filters
                })
            );
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.susbcription);
    }

    public filter() {
        this.filterChangesSubject.next();
    }

    public resetFilters() {
        this.filters.reset();
        this.filter();
    }
}
