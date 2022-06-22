import { Observable } from 'rxjs/Observable';
import { RequestOptionsArgs, Response } from '@angular/http';
import { LocalStorageService, SessionStorageService } from 'ng2-webstorage';
import { JhiHttpInterceptor } from 'ng-jhipster';
import { CookieService } from 'ngx-cookie';
import { InstallationService } from '../../shared';

export class AuthInterceptor extends JhiHttpInterceptor {
    constructor(
        private localStorage: LocalStorageService,
        private sessionStorage: SessionStorageService,
        private cookieService: CookieService,
        private installationService: InstallationService
    ) {
        super();
    }

    requestIntercept(options?: RequestOptionsArgs): RequestOptionsArgs {
        const token =
            this.localStorage.retrieve(
                this.installationService.getAuthenticationTokenByInstace()
            ) ||
            this.sessionStorage.retrieve(
                this.installationService.getAuthenticationTokenByInstace()
            );
        if (!!token) {
            options.headers.append('Authorization', 'Bearer ' + token);
        } else {
            const tokenFromCookie = this.cookieService.get(
                this.installationService.getAuthenticationTokenByInstace()
            );
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
            this.localStorage.store(
                this.installationService.getAuthenticationTokenByInstace(),
                jwt
            );
        } else {
            this.sessionStorage.store(
                this.installationService.getAuthenticationTokenByInstace(),
                jwt
            );
        }
    }
}
