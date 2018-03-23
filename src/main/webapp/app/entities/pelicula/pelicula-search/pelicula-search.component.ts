import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';
import { Subject, Subscription } from 'rxjs/Rx';
import { IdiomaService } from '../../idioma/idioma.service';
import { PeliculaFilter } from './pelicula-filter.model';

@Component({
    selector: 'ac-pelicula-search',
    templateUrl: './pelicula-search.component.html'
})

export class PeliculaSearchComponent implements OnInit, OnDestroy {

    private filterChangesSubject: Subject<any> = new Subject<any>();
    subscription: Subscription;

    @Input()
    filters: PeliculaFilter;

    constructor(
        private eventManager: JhiEventManager,
        private idiomaService: IdiomaService
    ) {}

    ngOnInit() {
        this.subscription = this.filterChangesSubject
            .debounceTime(300)
            .subscribe(() =>
                this.eventManager.broadcast({
                    name: 'peliculaSearch',
                    content: this.filters
                }));
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.subscription);
    }

    filter() {
        this.filters.batchSelection.selectedIds = [];
        this.filterChangesSubject.next();
    }

    resetFilters() {
        this.filters.reset();
        this.filter();
    }

    categoriaItemTemplate(item: any) {
        return item.nombre;
    }

    idiomaItemTemplate(item: any) {
        return item.nombre;
    }

    completeMethodIdiomas(event) {
        this.idiomaService.query({ query: `NOMBRE ILIKE '%${event.query}%'` })
            .subscribe((result) => this.filters.allIdiomas = result.json);
    }
}
