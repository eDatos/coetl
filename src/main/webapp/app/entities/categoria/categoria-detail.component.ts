import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Categoria } from './categoria.model';
import { CategoriaService } from './categoria.service';

@Component({
    selector: 'jhi-categoria-detail',
    templateUrl: './categoria-detail.component.html'
})
export class CategoriaDetailComponent implements OnInit, OnDestroy {

    categoria: Categoria;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private categoriaService: CategoriaService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCategorias();
    }

    load(id) {
        this.categoriaService.find(id).subscribe((categoria) => {
            this.categoria = categoria;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCategorias() {
        this.eventSubscriber = this.eventManager.subscribe(
            'categoriaListModification',
            (response) => this.load(this.categoria.id)
        );
    }
}
