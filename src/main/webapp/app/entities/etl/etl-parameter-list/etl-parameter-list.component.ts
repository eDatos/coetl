import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { Parameter } from '../../parameter/parameter.model';
import { Principal, ResponseWrapper } from '../../../shared';
import { EtlService } from '../etl.service';

@Component({
    selector: 'ac-etl-parameter-list',
    templateUrl: 'etl-parameter-list.component.html'
})
export class EtlParameterListComponent implements OnInit, OnDestroy {
    public static EVENT_NAME = 'etlParameterListModification';

    @Input() idEtl: number;

    currentAccount: Account;
    parameters: Parameter[];
    eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private etlService: EtlService,
        private principal: Principal,
        private translateService: TranslateService
    ) {}

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.loadAll();
        this.registerChangesInEtlParameter();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    existParameters(): boolean {
        return !!this.parameters && !!this.parameters.length;
    }

    getTypeName(parameter: Parameter): string {
        return this.translateService.instant(`coetlApp.parameter.type.${parameter.type}`);
    }

    trackId(index: number, item: Parameter) {
        return item.id;
    }

    private loadAll() {
        this.etlService
            .findAllParameters(this.idEtl)
            .subscribe((response: ResponseWrapper) => this.onSuccess(response.json));
    }

    private registerChangesInEtlParameter() {
        this.eventSubscriber = this.eventManager.subscribe(
            EtlParameterListComponent.EVENT_NAME,
            (response) => this.loadAll()
        );
    }

    private onSuccess(data: Parameter[]) {
        this.parameters = data;
    }
}
