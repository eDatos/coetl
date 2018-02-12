import { AfterViewChecked, Component, ElementRef, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

// <ac-side-menu [parent]="instance">
//    <button class="side-menu-item btn btn-frameless">Un botón</button>
//    <a class="side-menu-item" href="#">Un enlace</a>
// </ac-side-menu>
@Component({
    selector: 'ac-side-menu',
    templateUrl: 'side-menu.component.html',
    styleUrls: ['./side-menu.component.scss']
})
export class SideMenuComponent implements OnInit, AfterViewChecked {

    private TITLE_TAG = 'h3';
    private CLASS_HAS_MENU = 'has-menu';

    @Input()
    public parent: HasTitlesContainer;

    private fragment: string = null;

    public menu: any[] = [];

    private hasToScroll = false;

    constructor(
        private route: ActivatedRoute
    ) { }
    ngOnInit() {
        this.route.fragment.subscribe((fragment) => {
            this.fragment = fragment;
            this.hasToScroll = true;
        });
    }

    ngAfterViewChecked() {
        if (this.parent && this.parent.getTitlesContainer()) {
            const titlesContainerElement = this.parent.getTitlesContainer().nativeElement;
            this.addClassHasMenu(titlesContainerElement);

            // Workaround to play around h3 titles inside ngIf without complicating the component API
            setTimeout(() => {
                const titles = titlesContainerElement.querySelectorAll(this.TITLE_TAG);
                this.buildMenu(titles);
                this.scrollToFragment(titlesContainerElement, this.fragment);
            }, 0);

        }
    }

    addClassHasMenu(titlesContainerElement: HTMLElement) {
        titlesContainerElement.classList.add(this.CLASS_HAS_MENU);
    }

    buildMenu(titles: HTMLElement[]) {
        if (this.menu.length === 0) {
            for (let i = 0; i < titles.length; i++) {
                const element = titles[i];
                if (element.textContent) {
                    if (!element.id) {
                        element.id = this.htmlIdGenerator(element.textContent);
                    }
                    this.menu.push({
                        url: element.id,
                        title: element.textContent
                    });
                }
            };
        }
    }

    htmlIdGenerator(textContent: string) {
        const PREFIX = 'side-menu-id-';
        return PREFIX + textContent.replace(/\s+/g, '-').replace(/[^a-zA-Z0-9]/gmi, '');
    }

    scrollToFragment(titlesContainerElement: HTMLElement, fragment: string) {
        if (this.hasToScroll) {
            const elementTitle: HTMLElement = <HTMLElement>titlesContainerElement.querySelector('#' + this.fragment);
            if (elementTitle !== null) {
                const offset: number = this.calculateFixedNavbarOffsetBeforeScrollIntoView(titlesContainerElement);
                elementTitle.scrollIntoView();
                if (this.needsOffsetScrolling(elementTitle)) {
                    window.scrollBy(0, -offset);
                }
                this.hasToScroll = false;
            }
        }
    }

    // Ugly solution to take into account the fixed navbar after scrollIntoView
    calculateFixedNavbarOffsetBeforeScrollIntoView(titlesContainerElement: HTMLElement): number {
        // Lets move to the start of the page so getBoundingClientRect give us the correct value
        window.scroll(0, 0);
        // Lets see where is located the titles container
        let offset = titlesContainerElement.getBoundingClientRect().top;
        // Add the top padding
        offset += Number(window.getComputedStyle(titlesContainerElement).getPropertyValue('padding-top').replace('px', ''));
        // Hand adjusted
        offset += 15;
        return offset;
    }

    needsOffsetScrolling(elementTitle: HTMLElement) {
        return window.pageYOffset > elementTitle.offsetTop;
    }
}

export interface HasTitlesContainer {
    getTitlesContainer(): ElementRef;
}
