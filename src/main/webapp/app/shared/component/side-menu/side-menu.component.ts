import { AfterViewInit, Component, ElementRef, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ScrollService } from '../../service/scroll';

// <ac-side-menu [parent]="instance">
//    <button class="side-menu-item btn btn-frameless">Un botón</button>
//    <a class="side-menu-item" href="#">Un enlace</a>
// </ac-side-menu>
@Component({
    selector: 'ac-side-menu',
    templateUrl: 'side-menu.component.html',
    styleUrls: ['./side-menu.component.scss']
})
export class SideMenuComponent implements AfterViewInit {

    @Input()
    public parent: HasTitlesContainer;

    public menu: any[] = [];

    constructor(
        private route: ActivatedRoute,
        private scrollService: ScrollService
    ) { }

    ngAfterViewInit() {
        if (!this.parent || !this.parent.getTitlesContainer()) { return; }

        const titlesContainerElement: HTMLElement = this.parent.getTitlesContainer().nativeElement;
        titlesContainerElement.classList.add('has-menu');
        this.buildMenu(titlesContainerElement);

        this.route.fragment.subscribe((fragment) => {
            this.scrollService.scrollToFragment(titlesContainerElement, fragment);
        });

    }

    buildMenu(titlesContainerElement: HTMLElement) {
        if (this.menu.length > 0) { return; } // Already built
        setTimeout(() => {
            const titles = this.querySelectorAll(titlesContainerElement, 'h3');
            this.menu = titles.filter((element) => !!element.textContent)
                .map((element) => {
                    element.id = element.id || this.htmlIdGenerator(element.textContent);
                    return {
                        url: element.id,
                        title: element.textContent
                    };
                })
        }, 0);
    }

    querySelectorAll(htmlElement: HTMLElement, selector: string): any[] {
        const result = [];
        const elements = htmlElement.querySelectorAll(selector);
        for (let i = 0; i < elements.length; i++) {
            result.push(elements[i]);

        }
        return result;
    }

    htmlIdGenerator(textContent: string) {
        return 'side-menu-id-' + textContent.replace(/\s+/g, '-').replace(/[^a-zA-Z0-9]/gmi, '');
    }

}

export interface HasTitlesContainer {
    getTitlesContainer(): ElementRef;
}
