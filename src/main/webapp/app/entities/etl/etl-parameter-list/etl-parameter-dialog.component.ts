import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { EtlService } from '../etl.service';
import { Parameter, Typology, FileType } from '../../parameter';
import { EtlParameterListComponent } from './etl-parameter-list.component';

@Component({
    selector: 'ac-etl-parameter-dialog',
    templateUrl: 'etl-parameter-dialog.component.html',
    styleUrls: ['etl-parameter-dialog.component.scss']
})
export class EtlParameterDialogComponent implements OnInit {
    public parameter: Parameter;
    public isPassword: boolean;
    public isFile: boolean;
    public typologyEnum = Typology;
    public fileTypeEnum = FileType;
    public keys = Object.keys;
    public fieldTextType: boolean;
    public selectedFile: File;
    public acceptedFileTypes: String;

    public typology: Typology;

    constructor(
        private etlService: EtlService,
        private activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.isPassword = this.parameter.typology === Typology.PASSWORD ? true : false;
        this.isFile = this.parameter.typology === Typology.FILE ? true : false;
        this.typology = this.parameter.typology;
        this.setTypologyDefault();
        this.acceptedFileTypes = this.enumToString();
    }

    public toggleFieldTextType() {
        this.fieldTextType = !this.fieldTextType;
    }

    private enumToString(): string {
        const formatKeys = Object.keys(this.fileTypeEnum);
        const values: string[] = [];
        for (const key of formatKeys) {
            const value = this.fileTypeEnum[key];
            values.push(value);
        }
        return values.join(', ');
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

    public setTypeTypology(event: any) {
        switch (this.parameter.typology) {
            case Typology.PASSWORD:
                {
                    this.isPassword = true;
                    this.isFile = false;
                    this.parameter.value = '';
                }
                break;
            case Typology.FILE:
                {
                    this.isPassword = false;
                    this.isFile = true;
                    this.parameter.value = '';
                }
                break;
            default:
                this.parameter.value = '';
                this.isPassword = false;
                this.isFile = false;
                break;
        }
    }

    private trimValuesParameter() {
        this.parameter.value = this.parameter.value.trim();
        this.parameter.key = !!this.parameter.id ? this.parameter.key : this.parameter.key.trim();
    }

    private setFilenameAsValue() {
        this.parameter.value = this.selectedFile.name;
    }

    public onFileSelected(event: any) {
        this.selectedFile = event.target.files[0];
    }

    public save() {
        if (this.isFile) {
            this.setFilenameAsValue();
        }
        this.trimValuesParameter();
        const parameterEditObservable = !!this.parameter.id
            ? this.etlService.updateParameter(
                  this.parameter.etlId,
                  this.parameter,
                  this.selectedFile
              )
            : this.etlService.createParameter(
                  this.parameter.etlId,
                  this.parameter,
                  this.selectedFile
              );

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
