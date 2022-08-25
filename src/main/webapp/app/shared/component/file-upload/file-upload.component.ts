import {
    Component,
    ContentChild,
    EventEmitter,
    Input,
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
export class FileUploadComponent implements OnInit {
    @Input() public url: string;

    @Input() public title;

    @Input() public name = 'file';

    @Input() public maxFileSize = null;

    @Input() public disabled = false;

    @Input() public accept = false;

    @Input() public auto = true;

    @Input() public limited = 1;

    @Input() public files; // Puede ser un elemento o un array

    @Input() public showHelp = false;

    @Input() public helpTitle: string;

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
    ) {}

    ngOnInit() {
        if (this.auto) {
            this.mode = 'basic';
        } else {
            this.mode = 'advanced';
        }
        this.helpTranslatedTitle = this.helpTitle
            ? this.translateService.instant(this.helpTitle)
            : '';
    }

    onErrorMethod($event) {
        this.onError.emit($event);
    }

    upload() {
        if (this.auto) {
            throw new Error('Manual upload is not supported because upload mode is auto');
        }
        this.fileUpload.upload();
    }
}
