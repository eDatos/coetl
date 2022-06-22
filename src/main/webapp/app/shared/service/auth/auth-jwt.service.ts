import { Injectable, Injector } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService, SessionStorageService } from 'ng2-webstorage';
import { CookieService } from 'ngx-cookie';
import { TOKEN_AUTH_NAME } from '../../../app.constants';
import { InstallationService } from '../internal-installation';

@Injectable()
export class AuthServerProvider {
    private authenticationToken: string;
    private installationService;

    constructor(
        private $localStorage: LocalStorageService,
        private $sessionStorage: SessionStorageService,
        private cookieService: CookieService,
        private injector: Injector
    ) {
        this.installationService = this.injector.get(InstallationService);
        this.authenticationToken = this.installationService.getAuthenticationTokenByInstance();
    }

    getToken() {
        const token =
            this.$localStorage.retrieve(this.authenticationToken) ||
            this.$sessionStorage.retrieve(this.authenticationToken);
        if (!token) {
            return this.cookieService.get(this.authenticationToken);
        }
        return token;
    }

    loginWithToken(jwt, rememberMe) {
        if (jwt) {
            this.storeAuthenticationToken(jwt, rememberMe);
            return Promise.resolve(jwt);
        } else {
            return Promise.reject('auth-jwt-service Promise reject'); // Put appropriate error message here
        }
    }

    storeAuthenticationToken(jwt, rememberMe) {
        if (rememberMe) {
            this.$localStorage.store(this.authenticationToken, jwt);
        } else {
            this.$sessionStorage.store(this.authenticationToken, jwt);
        }
    }

    logout(): Observable<any> {
        return new Observable((observer) => {
            this.$localStorage.clear(this.authenticationToken);
            this.$sessionStorage.clear(this.authenticationToken);
            this.cookieService.remove(this.authenticationToken);
            observer.complete();
        });
    }
}
