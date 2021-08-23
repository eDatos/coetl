import { Component, OnInit } from '@angular/core';
import { ConfigService } from '../../config';
import { Observable } from 'rxjs/Observable';

declare const MetamacNavBar: {
    loadNavbar(obj?: { element: string }): void;
};

@Component({
    selector: 'ac-edatos-navbar',
    template: `
        <div class="edatos-navbar"></div>
        <nav id="edatos-navbar"></nav>
    `,
    styleUrls: ['./edatos-navbar.component.scss']
})
export class EdatosNavbarComponent implements OnInit {
    public isLoading = true;

    constructor(private configService: ConfigService) {}

    public ngOnInit() {
        const navbarScriptUrl = this.configService.getConfig().metadata.navbarScriptUrl;
        this.loadScript(`${navbarScriptUrl}/js/metamac-navbar.js`).subscribe(
            () => {
                this.isLoading = false;
                if (typeof MetamacNavBar === 'undefined') {
                    console.warn('Could not load eDatos navbar');
                } else {
                    MetamacNavBar.loadNavbar({
                        element: 'edatos-navbar'
                    });
                }
            },
            (error) => {
                console.error('Error al obtener el navbar', error);
            }
        );
    }

    private loadScript(url: string): Observable<Event> {
        return new Observable<Event>((observer) => {
            const scriptElement: HTMLScriptElement = document.createElement('script');
            scriptElement.src = url;

            scriptElement.onload = () => {
                observer.next();
                observer.complete();
            };

            scriptElement.onerror = (error: any) => {
                console.error('Error al obtener el navbar', error);
            };

            document.getElementsByTagName('head')[0].appendChild(scriptElement);
        });
    }
}
