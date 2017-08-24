import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Operacion } from './operacion.model';
import { OperacionService } from './operacion.service';

@Component({
    selector: 'jhi-operacion-detail',
    templateUrl: './operacion-detail.component.html'
})
export class OperacionDetailComponent implements OnInit, OnDestroy {

    operacion: Operacion;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private operacionService: OperacionService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInOperacions();
    }

    load(id) {
        this.operacionService.find(id).subscribe((operacion) => {
            this.operacion = operacion;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInOperacions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'operacionListModification',
            (response) => this.load(this.operacion.id)
        );
    }
}
