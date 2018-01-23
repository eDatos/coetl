import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { Categoria } from './categoria.model';
import { CategoriaService } from './categoria.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-categoria',
    templateUrl: './categoria.component.html'
})
export class CategoriaComponent implements OnInit, OnDestroy {
categorias: Categoria[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private categoriaService: CategoriaService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.categoriaService.query().subscribe(
            (res: ResponseWrapper) => {
                this.categorias = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInCategorias();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Categoria) {
        return item.id;
    }
    registerChangeInCategorias() {
        this.eventSubscriber = this.eventManager.subscribe('categoriaListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
