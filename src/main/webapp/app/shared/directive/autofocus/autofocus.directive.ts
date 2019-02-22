import { AfterViewInit, Directive, ElementRef, Renderer2 } from '@angular/core';

// Forked from https://shekhargulati.com/2017/12/02/adding-autofocus-to-an-input-field-in-an-angular-5-bootstrap-4-application/

/**
 * Directiva para realizar focus al primer elemento no desactivado y que sea editable. Si ninguno cumple la condición se creará un hermano invisible al que se le hará focus
 * Soporta la inclusión de este atributo en un elemento 'focusable'
 * (<input>, <textarea>, <ac-autocomplete>, <ac-calendar>, etc...); así como en un <fieldset>.
 * NO debe usarse dentro de un fieldset disabled. En ese caso, la directiva siempre debe estar asignada al fieldset
*/
@Directive({
    selector: '[acAutofocus]'
})
export class AutofocusDirective implements AfterViewInit {

    readonly focusableElements: string[] = ['input', 'textarea'];

    constructor(private el: ElementRef, private renderer: Renderer2) {
    }

    ngAfterViewInit() {
        setTimeout(() => {
            const htmlElement = this.el.nativeElement;
            this.focusOnFirstEditableElement(htmlElement);
        }, 0);
    }

    private focusOnFirstEditableElement(htmlElement: any) {
        const candidateChilds: HTMLElement[] = this.querySelectorAllFocusableElements(htmlElement);
        const firstEnabledChild: HTMLElement = candidateChilds.find((element: any) => {
            return !element.disabled;
        });

        let elementToFocus = firstEnabledChild || htmlElement;

        let focusableSibling = false;
        if (htmlElement.disabled) {
            focusableSibling = true;
            elementToFocus = this.createFocusableSibling(htmlElement);
        }

        elementToFocus.focus();

        if (focusableSibling) {
            this.renderer.removeChild(this.renderer.parentNode(elementToFocus), elementToFocus);
        }
    }

    private createFocusableSibling(node) {
        const sibling = this.renderer.createElement('div');
        this.renderer.setAttribute(sibling, 'tabindex', '0');
        this.renderer.appendChild(this.renderer.parentNode(node), sibling);
        return sibling;
    }

    private querySelectorAllFocusableElements(htmlElement: HTMLElement): HTMLElement[] {
        return Array.prototype.slice.call(htmlElement.querySelectorAll(this.focusableElements.join(', ')));
    }
}
