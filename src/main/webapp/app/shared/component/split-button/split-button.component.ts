import { AfterContentInit, Component, ElementRef, ViewChild } from '@angular/core';

@Component({
    selector: 'ac-split-button',
    templateUrl: 'split-button.component.html',
    styleUrls: ['./split-button.component.scss']
})
export class SplitButtonComponent implements AfterContentInit {
    public menuVisible = false;

    @ViewChild('otherButtonsWrapper')
    otherButtons: ElementRef;

    public hasOtherButtons = false;

    constructor(private element: ElementRef) {}

    ngAfterContentInit() {
        const acSplitButtonOthers = this.otherButtons.nativeElement.children[0];
        return (this.hasOtherButtons = acSplitButtonOthers
            ? acSplitButtonOthers.children.length > 0
            : false);
    }

    toggleMenu() {
        this.menuVisible = !this.menuVisible;
    }

    onFocusOut($event) {
        if (!this.element.nativeElement.contains($event.relatedTarget)) {
            // https://github.com/angular/angular/issues/17572
            setTimeout(() => (this.menuVisible = false), 0);
        }
    }
}
