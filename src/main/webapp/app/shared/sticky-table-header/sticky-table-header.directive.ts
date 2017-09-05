import { Directive, ElementRef, AfterViewInit } from '@angular/core';
import * as $ from 'jquery';

@Directive({
    selector: '[acStickyTableHeader]'
})
export class StickyTableHeaderDirective implements AfterViewInit {

    constructor(
        private el: ElementRef
    ) { }

    ngAfterViewInit() {
        const offset = $(this.el.nativeElement)[0].getBoundingClientRect().top;
        $(this.el.nativeElement).stickyTableHeaders({ fixedOffset: offset });
    }
}
