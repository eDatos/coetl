import { Directive, ElementRef, AfterViewChecked, DoCheck } from '@angular/core';
import { Observable } from 'rxjs';

@Directive({ selector: '[acMakeFixedRoom]' })
export class MakeFixedRoomDirective implements AfterViewChecked, DoCheck {
    private currentHeight = 0;
    private pageTitle: HTMLElement;

    constructor(private el: ElementRef) {
        Observable.fromEvent(window, 'resize')
            .debounceTime(150)
            .subscribe(() => {
                this.updatePadding();
            });
    }

    ngDoCheck() {
        this.updatePadding();
    }

    ngAfterViewChecked() {
        this.pageTitle = this.el.nativeElement.querySelector('.entity-header');
        if (!this.pageTitle) {
            console.warn('Required .entity-header element');
        } else {
            this.el.nativeElement.classList.add('has-entity-header');
        }
    }

    updatePadding() {
        if (this.pageTitle && this.pageTitle.clientHeight !== this.currentHeight) {
            this.currentHeight = this.pageTitle.clientHeight;
            this.el.nativeElement.style.paddingTop = this.currentHeight + 'px';
        }
    }
}
