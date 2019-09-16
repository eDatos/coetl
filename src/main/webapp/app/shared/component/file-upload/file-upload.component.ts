import {
    Component,
    ContentChild,
    EventEmitter,
    Input,
    OnChanges,
    OnInit,
    Output,
    TemplateRef,
    ViewChild
} from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { FileUpload } from 'primeng/primeng';
import { AcAlertService } from '../alert/alert.service';
import { FileService } from '../../../entities/file/file.service';
import { CookieService } from 'ngx-cookie';

@Component({
    selector: 'ac-file-upload',
    templateUrl: 'file-upload.component.html',
    styleUrls: ['file-upload.component.scss']
})
export class FileUploadComponent implements OnInit, OnChanges {
    @Input() public url: string;

    @Input() public title;

    @Input() public name = 'file';

    @Input() public maxFileSize = null;

    @Input() public disabled = false;

    @Input() public accept = false;

    @Input() public auto = true;

    @Input() public limited = 1;

    @Input() public files; // Puede ser un elemento o un array

    @Input() public funcionDescargar;

    @Input() public showHelp = false;

    @Input() public helpTitle: string;

    public innerFiles;

    public mode;

    public helpTranslatedTitle: string;

    @ContentChild(TemplateRef) actionsTemplate: TemplateRef<any>;

    @Output() private onUpload: EventEmitter<any> = new EventEmitter();

    @Output() private onError: EventEmitter<any> = new EventEmitter();

    @ViewChild(FileUpload) public fileUpload: FileUpload;

    constructor(
        private translateService: TranslateService,
        private fileService: FileService,
        private alertService: AcAlertService,
        private cookieService: CookieService
    ) {
        this.innerFiles = Array.isArray(this.files) ? this.files : this.files ? [this.files] : [];
    }

    ngOnInit() {
        if (this.auto) {
            this.mode = 'basic';
        } else {
            this.mode = 'advanced';
        }
        this.helpTranslatedTitle = this.helpTitle
            ? this.translateService.instant(this.helpTitle)
            : '';

        this.funcionDescargar = this.funcionDescargar || this.download;
    }

    ngOnChanges(changes) {
        if (changes.files && changes.files.currentValue !== changes.files.previousValue) {
            this.innerFiles = Array.isArray(this.files)
                ? this.files
                : this.files
                ? [this.files]
                : [];
        }
    }

    onUploadMethod($event) {
        this.onUpload.emit($event);
    }

    onErrorMethod($event) {
        this.onError.emit($event);
    }

    onSelectMethod($event) {
        const files: File[] = Array.from($event.files);
        const emptyFiles: File[] = files.filter((fichero) => fichero.size === 0);
        if (emptyFiles.length > 0) {
            const codeErrorFileEmpty =
                this.limited > 1 ? 'error.file.multi.empty' : 'error.file.single.empty';
            const messageFileEmpty = this.translateService.instant(codeErrorFileEmpty, [
                emptyFiles.map((fichero) => fichero.name).join(', ')
            ]);
            this.alertService.error(messageFileEmpty);
        }
        if (this.fileUpload.msgs.length > 0) {
            this.fileUpload.msgs.forEach((message) => {
                this.alertService.error(message.summary + ' ' + message.detail);
            });
        }
    }

    onBeforeSendMethod($event) {
        $event.xhr.setRequestHeader('X-XSRF-TOKEN', this.cookieService.get('XSRF-TOKEN'));
    }

    download(file) {
        this.fileService.download(file.id);
    }

    canUpload() {
        return (
            !this.limited ||
            (this.limited > 0 && this.innerFiles && this.innerFiles.length < this.limited)
        );
    }

    upload() {
        if (this.auto) {
            throw new Error('Manual upload is not supported because upload mode is auto');
        }
        this.fileUpload.upload();
    }
}
