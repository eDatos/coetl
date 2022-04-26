import { Observable } from 'rxjs/Observable';
import { RequestOptionsArgs, Response } from '@angular/http';
import { LocalStorageService, SessionStorageService } from 'ng2-webstorage';
import { JhiHttpInterceptor } from 'ng-jhipster';
import { CookieService } from 'ngx-cookie';
import { TOKEN_AUTH_NAME } from '../../app.constants';

export class AuthInterceptor extends JhiHttpInterceptor {
    constructor(
        private localStorage: LocalStorageService,
        private sessionStorage: SessionStorageService,
        private cookieService: CookieService
    ) {
        super();
    }

    requestIntercept(options?: RequestOptionsArgs): RequestOptionsArgs {
        const token =
            this.localStorage.retrieve(TOKEN_AUTH_NAME) ||
            this.sessionStorage.retrieve(TOKEN_AUTH_NAME);
        if (!!token) {
            options.headers.append('Authorization', 'Bearer ' + token);
        } else {
            const tokenFromCookie = this.cookieService.get(TOKEN_AUTH_NAME);
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
            this.localStorage.store(TOKEN_AUTH_NAME, jwt);
        } else {
            this.sessionStorage.store(TOKEN_AUTH_NAME, jwt);
        }
    }
}
