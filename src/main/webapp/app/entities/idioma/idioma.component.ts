import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { Idioma } from './idioma.model';
import { IdiomaService } from './idioma.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-idioma',
    templateUrl: './idioma.component.html'
})
export class IdiomaComponent implements OnInit, OnDestroy {
idiomas: Idioma[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private idiomaService: IdiomaService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.idiomaService.query().subscribe(
            (res: ResponseWrapper) => {
                this.idiomas = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInIdiomas();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Idioma) {
        return item.id;
    }
    registerChangeInIdiomas() {
        this.eventSubscriber = this.eventManager.subscribe('idiomaListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
