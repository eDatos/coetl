import { Directive, ElementRef, AfterViewInit, Input } from '@angular/core';
import * as $ from 'jquery';

@Directive({
    selector: '[acStickyTableHeader]'
})
export class StickyTableHeaderDirective implements AfterViewInit {

    @Input()
    stickyPreviousElement: string;

    constructor(
        private el: ElementRef
    ) { }

    ngAfterViewInit() {
        const $previousElement = $(this.stickyPreviousElement);
        if ($previousElement.length) {
            const offset = $previousElement[0].getBoundingClientRect().bottom;
            $(this.el.nativeElement).stickyTableHeaders({ fixedOffset: offset });
        }
    }
}
