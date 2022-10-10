import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { Parameter, Typology } from '../../../entities/parameter';
import { GloablParameterService } from '../global-parameter.service';
import { GlobalParameterComponent } from '../global-parameter.component';

@Component({
    selector: 'ac-global-parameter-dialog',
    templateUrl: 'global-parameter-dialog.component.html',
    styleUrls: ['global-parameter-dialog.component.scss']
})
export class GlobalParameterDialogComponent implements OnInit {
    public parameter: Parameter;
    public isPassword: boolean;
    public typologyEnum = Typology;
    public keys = Object.keys;
    public fieldTextType: boolean;

    public typology: Typology;

    constructor(
        private globalParameterService: GloablParameterService,
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
        if (this.parameter.typology === Typology.PASSWORD) {
            this.isPassword = true;
        } else {
            this.isPassword = false;
            this.parameter.value = '';
        }
    }

    public save() {
        const parameterEditObservable = !!this.parameter.id
            ? this.globalParameterService.updateParameter(this.parameter)
            : this.globalParameterService.createParameter(this.parameter);

        this.subscribeToSaveResponse(parameterEditObservable);
    }

    public clear() {
        this.activeModal.dismiss('closed');
    }

    private subscribeToSaveResponse(observable: Observable<Parameter>) {
        observable.subscribe((parameter) => {
            this.eventManager.broadcast({
                name: GlobalParameterComponent.EVENT_NAME,
                content: parameter
            });
            this.activeModal.close('saved');
        });
    }
}
