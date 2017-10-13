import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'jhi-error',
    templateUrl: './error.component.html'
})
export class ErrorComponent implements OnInit {
    public errorMessage: string;
    public error403: boolean;
    public error404: boolean;

    public title: string;
    public detail: string;

    constructor(private route: ActivatedRoute) { }

    ngOnInit() {
        this.route.data.subscribe((routeData) => {
            this.title = 'error.title';

            if (routeData.error403) {
                this.title = 'error.403.title';
                this.detail = 'error.403.detail';
            }

            if (routeData.error404) {
                this.title = 'error.404.title';
                this.detail = 'error.404.detail';
            }

            if (routeData.errorMessage) {
                this.errorMessage = routeData.errorMessage;
            }
        });
    }
}
