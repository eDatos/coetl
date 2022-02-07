import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { EtlService } from '../etl.service';
import { Parameter, Typology } from '../../parameter';
import { EtlParameterListComponent } from './etl-parameter-list.component';

@Component({
    selector: 'ac-etl-parameter-dialog',
    templateUrl: 'etl-parameter-dialog.component.html'
})
export class EtlParameterDialogComponent implements OnInit {
    public parameter: Parameter;
    public isPassword: boolean;
    public typologyEnum = Typology;
    public keys = Object.keys;

    private flag = false;

    constructor(
        private etlService: EtlService,
        private activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.isPassword = this.parameter.typology === Typology.PASSWORD ? true : false;
        this.setTypologyDefault();
    }

    private setTypologyDefault() {
        if (this.parameter.typology === undefined) {
            this.parameter.typology = Typology.GENERIC;
        }
    }

    public isPasswordTypology(event: any) {
        if (this.parameter.typology === Typology.GENERIC && !this.flag) {
            this.parameter.value = atob(this.parameter.value);
            this.flag = true;
        }
        this.isPassword = this.parameter.typology === Typology.PASSWORD ? true : false;
    }

    public save() {
        const parameterEditObservable = !!this.parameter.id
            ? this.etlService.updateParameter(this.parameter.etlId, this.parameter)
            : this.etlService.createParameter(this.parameter.etlId, this.parameter);

        this.subscribeToSaveResponse(parameterEditObservable);
    }

    public clear() {
        this.activeModal.dismiss('closed');
    }

    private subscribeToSaveResponse(observable: Observable<Parameter>) {
        observable.subscribe((parameter) => {
            this.eventManager.broadcast({
                name: EtlParameterListComponent.EVENT_NAME,
                content: parameter
            });
            this.activeModal.close('saved');
        });
    }
}
