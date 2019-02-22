import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService, SessionStorageService } from 'ng2-webstorage';
import { CookieService } from 'ngx-cookie';

@Injectable()
export class AuthServerProvider {
    constructor(
        private $localStorage: LocalStorageService,
        private $sessionStorage: SessionStorageService,
        private cookieService: CookieService,
    ) { }

    getToken() {
        const token = this.$localStorage.retrieve('authenticationToken') || this.$sessionStorage.retrieve('authenticationToken');
        if (!token) {
            return this.cookieService.get('jhi-authenticationtoken');
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
            this.$localStorage.store('authenticationToken', jwt);
        } else {
            this.$sessionStorage.store('authenticationToken', jwt);
        }
    }

    logout(): Observable<any> {
        return new Observable((observer) => {
            this.$localStorage.clear('authenticationToken');
            this.$sessionStorage.clear('authenticationToken');
            this.cookieService.remove('jhi-authenticationtoken');
            observer.complete();
        });
    }
}
