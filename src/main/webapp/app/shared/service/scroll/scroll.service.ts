import { Injectable } from '@angular/core';

@Injectable()
export class ScrollService {
    constructor() {}

    scrollToFragment(titlesContainerElement: HTMLElement, fragment: string) {
        if (!titlesContainerElement) {
            return;
        }

        const elementTitle: HTMLElement = <HTMLElement>(
            titlesContainerElement.querySelector('#' + fragment)
        );
        if (!elementTitle) {
            return;
        }

        const offset: number = this.calculateFixedNavbarOffsetBeforeScrollIntoView(
            titlesContainerElement
        );
        elementTitle.scrollIntoView();
        if (this.needsOffsetScrolling(elementTitle)) {
            window.scrollBy(0, -offset);
        }
    }

    // Ugly solution to take into account the fixed navbar after scrollIntoView
    calculateFixedNavbarOffsetBeforeScrollIntoView(titlesContainerElement: HTMLElement): number {
        // Lets move to the start of the page so getBoundingClientRect give us the correct value
        window.scroll(0, 0);
        // Lets see where is located the titles container
        let offset = titlesContainerElement.getBoundingClientRect().top;
        // Add the top padding
        offset += Number(
            window
                .getComputedStyle(titlesContainerElement)
                .getPropertyValue('padding-top')
                .replace('px', '')
        );
        // Hand adjusted
        offset += 15;
        return offset;
    }

    needsOffsetScrolling(elementTitle: HTMLElement) {
        return window.pageYOffset > elementTitle.offsetTop;
    }
}
