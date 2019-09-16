import { Component, OnInit, Input, HostBinding } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'ac-spinner',
    templateUrl: 'spinner.component.html',
    styleUrls: ['spinner.component.scss']
})
export class SpinnerComponent implements OnInit {
    @Input()
    altText = this.translateService.instant('global.loading');

    @Input()
    @HostBinding('class.overlay')
    overlay: boolean;

    constructor(private translateService: TranslateService) {}

    ngOnInit() {}
}
