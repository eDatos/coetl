import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Idioma } from './idioma.model';
import { IdiomaService } from './idioma.service';

@Component({
    selector: 'jhi-idioma-detail',
    templateUrl: './idioma-detail.component.html'
})
export class IdiomaDetailComponent implements OnInit, OnDestroy {

    idioma: Idioma;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private idiomaService: IdiomaService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInIdiomas();
    }

    load(id) {
        this.idiomaService.find(id).subscribe((idioma) => {
            this.idioma = idioma;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInIdiomas() {
        this.eventSubscriber = this.eventManager.subscribe(
            'idiomaListModification',
            (response) => this.load(this.idioma.id)
        );
    }
}
