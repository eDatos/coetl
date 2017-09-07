import { Component } from '@angular/core';

@Component({
    selector: 'ac-split-button',
    templateUrl: 'split-button.component.html',
    styleUrls: ['./split-button.component.scss']
})
export class SplitButtonComponent {

    public menuVisible = false;

    constructor() { }

    onDropdownButtonClick(event: Event) {
        this.show();
    }

    show() {
        this.menuVisible = !this.menuVisible;
    }

}
