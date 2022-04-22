import { Observable } from 'rxjs/Observable';
import { RequestOptionsArgs, Response } from '@angular/http';
import { LocalStorageService, SessionStorageService } from 'ng2-webstorage';
import { JhiHttpInterceptor } from 'ng-jhipster';
import { CookieService } from 'ngx-cookie';
import { JHI_TOKEN_AUTH_NAME, TOKEN_AUTH_NAME } from '../../app.constants';

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
            console.log(
                'HEADER   TOKEN == TRUE localStore.retrive/sessionStore.retrive         ',
                token
            );
            options.headers.append('Authorization', 'Bearer ' + token);
        } else {
            const tokenFromCookie = this.cookieService.get(TOKEN_AUTH_NAME);
            if (!!tokenFromCookie) {
                console.log('tokenFromCookie     cookieService.GET       ', tokenFromCookie);
                this.storeAuthenticationToken(tokenFromCookie, false);
                options.headers.append('Authorization', 'Bearer ' + tokenFromCookie);
            }
            console.log('tokenFromCookie == FALSE       ', tokenFromCookie);
        }
        console.log('OPTIONS       ', options);
        return options;
    }

    responseIntercept(observable: Observable<Response>): Observable<Response> {
        return observable; // by pass
    }

    private storeAuthenticationToken(jwt, rememberMe) {
        if (rememberMe) {
            this.localStorage.store(TOKEN_AUTH_NAME, jwt);
        } else {
            console.log('SIEMPRE ENTRO PORQUE REMEMBERME==FALSE       ', jwt);
            this.sessionStorage.store(TOKEN_AUTH_NAME, jwt);
        }
    }
}
