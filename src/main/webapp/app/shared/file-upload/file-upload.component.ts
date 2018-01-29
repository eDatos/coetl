import { Component, ContentChild, EventEmitter, Input, OnChanges, OnInit, Output, TemplateRef, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { JhiAlertService } from 'ng-jhipster';
import { FileUpload } from 'primeng/primeng';

import { DocumentoService } from '../../entities/documento/documento.service';

@Component({
    selector: 'ac-file-upload',
    templateUrl: 'file-upload.component.html',
    styleUrls: ['file-upload.component.scss']
})
export class FileUploadComponent implements OnInit, OnChanges {

    @Input()
    public url: string;

    @Input()
    public title;

    @Input()
    public name = 'file';

    @Input()
    public maxFileSize = null;

    @Input()
    public disabled = false;

    @Input()
    public accept = false;

    @Input()
    public mode = 'basic';

    @Input()
    public auto = true;

    @Input()
    public files; // Puede ser un elemento o un array

    @Input()
    public limited = 1;

    public innerFiles;

    @ContentChild(TemplateRef)
    actionsTemplate: TemplateRef<any>;

    @Output()
    private onUpload: EventEmitter<any> = new EventEmitter();

    @ViewChild(FileUpload)
    public fileUpload: FileUpload;

    constructor(
        private translateService: TranslateService,
        private documentoService: DocumentoService,
        private alertService: JhiAlertService
    ) { }

    ngOnInit() { }

    ngOnChanges(changes) {
        if (changes.files.currentValue !== changes.files.previousValue) {
            this.innerFiles = Array.isArray(this.files) ? this.files : this.files ? [this.files] : [];
        }
    }

    onUploadMethod($event) {
        this.onUpload.emit($event);
    }

    onSelectMethod($event) {
        if (this.fileUpload.msgs.length > 0) {
            this.fileUpload.msgs.forEach((message) => {
                this.alertService.error(message.summary + ' ' + message.detail, null, null);
            });
        }
    }

    download(file) {
        this.documentoService.download(file.id);
    }

    canUpload() {
        return !this.limited || this.limited > 0 && this.innerFiles && this.innerFiles.length < this.limited;
    }
}
