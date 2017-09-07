import { Component, ViewChild, ElementRef } from '@angular/core';

@Component({
    selector: 'ac-split-button',
    templateUrl: 'split-button.component.html',
    styleUrls: ['./split-button.component.scss']
})
export class SplitButtonComponent {

    public menuVisible = false;

    @ViewChild('otherButtonsWrapper')
    otherButtons: ElementRef;

    constructor() { }

    onDropdownButtonClick(event: Event) {
        this.show();
    }

    hasOtherButtons() {
        const acSplitButtonOthers = this.otherButtons.nativeElement.children[0];
        return acSplitButtonOthers ? acSplitButtonOthers.children.length > 0 : false;
    }

    show() {
        this.menuVisible = !this.menuVisible;
    }

}
