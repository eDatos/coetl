import { Observable } from 'rxjs/Observable';
import { RequestOptionsArgs, Response } from '@angular/http';
import { LocalStorageService, SessionStorageService } from 'ng2-webstorage';
import { JhiHttpInterceptor } from 'ng-jhipster';
import { CookieService } from 'ngx-cookie';
import { InstallationService } from '../../shared';
import { Injector } from '@angular/core';

export class AuthInterceptor extends JhiHttpInterceptor {
    private authenticationToken: string;
    private installationService;

    constructor(
        private localStorage: LocalStorageService,
        private sessionStorage: SessionStorageService,
        private cookieService: CookieService,
        private injector: Injector
    ) {
        super();
        this.installationService = this.injector.get(InstallationService);
        this.authenticationToken = this.installationService.getAuthenticationTokenByInstace();
    }

    requestIntercept(options?: RequestOptionsArgs): RequestOptionsArgs {
        const token =
            this.localStorage.retrieve(this.authenticationToken) ||
            this.sessionStorage.retrieve(this.authenticationToken);
        if (!!token) {
            options.headers.append('Authorization', 'Bearer ' + token);
        } else {
            const tokenFromCookie = this.cookieService.get(this.authenticationToken);
            if (!!tokenFromCookie) {
                this.storeAuthenticationToken(tokenFromCookie, false);
                options.headers.append('Authorization', 'Bearer ' + tokenFromCookie);
            }
        }
        return options;
    }

    responseIntercept(observable: Observable<Response>): Observable<Response> {
        return observable; // by pass
    }

    private storeAuthenticationToken(jwt, rememberMe) {
        if (rememberMe) {
            this.localStorage.store(this.authenticationToken, jwt);
        } else {
            this.sessionStorage.store(this.authenticationToken, jwt);
        }
    }
}
