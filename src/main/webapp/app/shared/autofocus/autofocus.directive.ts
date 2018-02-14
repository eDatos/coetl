import { AfterViewInit, Directive, ElementRef } from '@angular/core';

// Forked from https://shekhargulati.com/2017/12/02/adding-autofocus-to-an-input-field-in-an-angular-5-bootstrap-4-application/

@Directive({
    selector: '[acAutofocus]'
})
export class AutofocusDirective implements AfterViewInit {

    constructor(private el: ElementRef) {
    }

    ngAfterViewInit() {
        this.el.nativeElement.focus();
    }

}
