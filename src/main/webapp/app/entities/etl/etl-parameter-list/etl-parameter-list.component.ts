import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ResponseWrapper, GenericModalService } from '../../../shared';
import { Parameter, Type, Typology } from '../../parameter/parameter.model';
import { EtlService } from '../etl.service';
import { EtlParameterDialogComponent } from './etl-parameter-dialog.component';
import { EtlParameterDeleteDialogComponent } from './etl-parameter-delete-dialog.component';

@Component({
    selector: 'ac-etl-parameter-list',
    templateUrl: 'etl-parameter-list.component.html',
    styleUrls: ['etl-parameter-list.component.scss']
})
export class EtlParameterListComponent implements OnInit, OnDestroy {
    public static EVENT_NAME = 'etlParameterListModification';

    @Input() idEtl: number;

    parameters: Parameter[];
    eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private etlService: EtlService,
        private genericModalService: GenericModalService,
        private translateService: TranslateService
    ) {}

    ngOnInit() {
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

    editParameter(parameter?: Parameter) {
        let copy = new Parameter();
        if (!!parameter) {
            copy = Object.assign(copy, parameter);
            if (Typology.PASSWORD === parameter.typology) {
                this.etlService
                    .decodeParameter(parameter.etlId, parameter.id)
                    .subscribe((response) => {
                        this.openEditParameterDialog(response);
                    });
            } else {
                this.openEditParameterDialog(copy);
            }
        } else {
            copy.etlId = this.idEtl;
            copy.type = Type.MANUAL;
            this.openEditParameterDialog(copy);
        }
    }

    private openEditParameterDialog(parameter: Parameter) {
        this.genericModalService.open(
            EtlParameterDialogComponent as Component,
            { parameter },
            { container: '.app' }
        );
    }

    deleteParameter(parameter: Parameter) {
        const copy = Object.assign(new Parameter(), parameter);
        this.genericModalService.open(
            EtlParameterDeleteDialogComponent as Component,
            { parameter: copy },
            { container: '.app' }
        );
    }

    public isPasswordTypology(parameter: Parameter): boolean {
        return parameter.typology === Typology.PASSWORD ? true : false;
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
