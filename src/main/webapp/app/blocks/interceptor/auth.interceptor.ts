import { Observable } from 'rxjs/Observable';
import { RequestOptionsArgs, Response } from '@angular/http';
import { LocalStorageService, SessionStorageService } from 'ng2-webstorage';
import { JhiHttpInterceptor } from 'ng-jhipster';
import { CookieService } from 'ngx-cookie';

export class AuthInterceptor extends JhiHttpInterceptor {

    constructor(
        private localStorage: LocalStorageService,
        private sessionStorage: SessionStorageService,
        private cookieService: CookieService,

    ) {
        super();
    }

    requestIntercept(options?: RequestOptionsArgs): RequestOptionsArgs {
        const token = this.localStorage.retrieve('authenticationToken') || this.sessionStorage.retrieve('authenticationToken');
        if (!!token) {
            options.headers.append('Authorization', 'Bearer ' + token);
        } else {
            const tokenFromCookie = this.cookieService.get('jhi-authenticationtoken');
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
            this.localStorage.store('authenticationtoken', jwt);
        } else {
            this.sessionStorage.store('authenticationtoken', jwt);
        }
    }

}
