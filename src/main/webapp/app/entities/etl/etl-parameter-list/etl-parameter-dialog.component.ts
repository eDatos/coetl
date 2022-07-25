import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { EtlService } from '../etl.service';
import { Parameter, Typology } from '../../parameter';
import { EtlParameterListComponent } from './etl-parameter-list.component';

@Component({
    selector: 'ac-etl-parameter-dialog',
    templateUrl: 'etl-parameter-dialog.component.html',
    styleUrls: ['etl-parameter-dialog.component.scss']
})
export class EtlParameterDialogComponent implements OnInit {
    public parameter: Parameter;
    public isPassword: boolean;
    public typologyEnum = Typology;
    public keys = Object.keys;
    public fieldTextType: boolean;

    public typology: Typology;

    constructor(
        private etlService: EtlService,
        private activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.isPassword = this.parameter.typology === Typology.PASSWORD ? true : false;
        this.typology = this.parameter.typology;
        this.setTypologyDefault();
    }

    public toggleFieldTextType() {
        this.fieldTextType = !this.fieldTextType;
    }

    private setTypologyDefault() {
        if (this.parameter.typology === undefined) {
            this.parameter.typology = Typology.GENERIC;
        }
    }

    public isPasswordTypology(event: any) {
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
