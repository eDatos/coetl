import { Component, Input } from '@angular/core';

@Component({
    selector: 'ac-help-tooltip',
    template: '<i class="fa fa-info-circle" [placement]="position" [ngbTooltip]="label"></i>'
})
export class HelpTooltipComponent {
    @Input() label: string;

    @Input() position: 'top' | 'right' | 'left' | 'bottom' = 'right';

    constructor() {}
}
