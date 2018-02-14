import { AfterViewInit, Directive, ElementRef, Renderer2 } from '@angular/core';

// Forked from https://shekhargulati.com/2017/12/02/adding-autofocus-to-an-input-field-in-an-angular-5-bootstrap-4-application/

/**
 * Directiva para realizar focus al primer elemento no desactivado y que sea editable.
 * Soporta la inclusión de este atriubto en un elemento editable
 * (<input>, <textarea>, <ac-autocomplete>, <ac-calendar>, etc...); así como en un <fieldset>.
*/
@Directive({
    selector: '[acAutofocus]'
})
export class AutofocusDirective implements AfterViewInit {

    readonly editableElements: string[] = ['input', 'textarea'];

    constructor(private el: ElementRef, private renderer: Renderer2) {
    }

    ngAfterViewInit() {
        setTimeout(() => {
            const htmlElement = this.el.nativeElement;
            this.focusOnFirstEditableElement(htmlElement);
        }, 0);
    }

    private focusOnFirstEditableElement(htmlElement: any) {
        if (htmlElement.disabled) { return; }

        const candidateChilds: HTMLElement[] = this.querySelectorAllEditableElements(htmlElement);
        const firstEnabledChild: HTMLElement = candidateChilds.find((element: any) => {
            return !element.disabled;
        });

        const elementToFocus = firstEnabledChild || htmlElement;
        this.renderer.selectRootElement(elementToFocus).focus();
    }

    private querySelectorAllEditableElements(htmlElement: HTMLElement): HTMLElement[] {
        return Array.prototype.slice.call(htmlElement.querySelectorAll(this.editableElements.join(', ')));
    }
}
