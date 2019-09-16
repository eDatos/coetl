import { Component, ContentChild, Input, TemplateRef } from '@angular/core';

@Component({
    selector: 'ac-steps',
    templateUrl: 'steps.component.html',
    styleUrls: ['steps.component.scss']
})
export class StepsComponent {
    @Input()
    public steps: any[];

    @Input()
    public isPastStep: Function;

    @Input()
    public isCurrentStep: Function;

    @Input()
    public stepAction: Function;

    @ContentChild(TemplateRef)
    public titleTemplate: TemplateRef<any>;

    constructor() {}

    public hasStepAction(): boolean {
        return !!this.stepAction;
    }
}
