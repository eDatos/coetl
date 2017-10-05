import { Directive, ElementRef, AfterViewInit, DoCheck } from '@angular/core';
import { Observable } from 'rxjs';
import * as $ from 'jquery';

@Directive({
    selector: '[acStickyTableHeader]'
})
export class StickyTableHeaderDirective implements AfterViewInit, DoCheck {

    private currentOffset = 0;

    private initialized = false;

    constructor(private el: ElementRef) {
        Observable
            .fromEvent(window, 'resize')
            .debounceTime(150)
            .subscribe(() => {
                this.updateStickyTableHeader();
            });
    }

    ngDoCheck() {
        const newOffset = this.calculateCurrentOffset();
        if (this.currentOffset !== newOffset) {
            this.currentOffset = newOffset;
            this.updateStickyTableHeader();
        }
    }

    ngAfterViewInit() {
        if (!this.initialized) {
            this.initialized = true;
            this.currentOffset = this.calculateCurrentOffset();
            this.updateStickyTableHeader();
        }
    }

    calculateCurrentOffset() {
        return this.el.nativeElement.getBoundingClientRect().top + window.scrollY;
    }

    updateStickyTableHeader() {
        $(this.el.nativeElement).stickyTableHeaders('destroy');
        $(this.el.nativeElement).stickyTableHeaders({ fixedOffset: this.currentOffset + 1 });
    }
}
