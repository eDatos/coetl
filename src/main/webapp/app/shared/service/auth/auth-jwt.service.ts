import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService, SessionStorageService } from 'ng2-webstorage';
import { CookieService } from 'ngx-cookie';
import { InstallationService } from '../internal-installation';

@Injectable()
export class AuthServerProvider {
    constructor(
        private $localStorage: LocalStorageService,
        private $sessionStorage: SessionStorageService,
        private cookieService: CookieService,
        private installationService: InstallationService
    ) {}

    getToken() {
        const token =
            this.$localStorage.retrieve(
                this.installationService.getAuthenticationTokenByInstace()
            ) ||
            this.$sessionStorage.retrieve(
                this.installationService.getAuthenticationTokenByInstace()
            );
        if (!token) {
            return this.cookieService.get(
                this.installationService.getAuthenticationTokenByInstace()
            );
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
            this.$localStorage.store(
                this.installationService.getAuthenticationTokenByInstace(),
                jwt
            );
        } else {
            this.$sessionStorage.store(
                this.installationService.getAuthenticationTokenByInstace(),
                jwt
            );
        }
    }

    logout(): Observable<any> {
        return new Observable((observer) => {
            this.$localStorage.clear(this.installationService.getAuthenticationTokenByInstace());
            this.$sessionStorage.clear(this.installationService.getAuthenticationTokenByInstace());
            this.cookieService.remove(this.installationService.getAuthenticationTokenByInstace());
            observer.complete();
        });
    }
}
