import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'ac-entity-list-empty',
    templateUrl: './entity-list-empty.component.html',
    styleUrls: ['./entity-list-empty.component.scss']
})
export class EntityListEmptyComponent {

    @Input()
    buttonLink: string[];

    @Input()
    buttonLabel: string;

    @Input()
    operacionButton: string[] = [];

    constructor(
        private router: Router
    ) { }

    createNewEntity() {
        this.router.navigate(this.buttonLink);
    }
}
