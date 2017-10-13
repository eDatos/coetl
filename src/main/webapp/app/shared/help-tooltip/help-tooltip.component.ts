import { Component, Input } from '@angular/core';

@Component({
    selector: 'ac-help-tooltip',
    template: '<i class="fa fa-info-circle" placement="right" [ngbTooltip]="label"></i>'
})

export class HelpTooltipComponent {

    @Input()
    label: string;

    constructor() { }
}
