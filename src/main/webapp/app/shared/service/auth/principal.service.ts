import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import { UserService } from '../user/user.service';
import { Account } from '../user/account.model';
import { Rol } from './rol.model';

@Injectable()
export class Principal {
    [x: string]: any;
    private userIdentity: Account;
    private authenticated = false;
    private authenticationState = new Subject<any>();

    constructor(
        private userService: UserService
    ) { }

    authenticate(identity) {
        this.userIdentity = identity;
        this.authenticated = identity !== null;
        this.authenticationState.next(this.userIdentity);
    }

    hasRoles(rolesRuta: Rol[]): Promise<boolean> {
        return Promise.resolve(this.rolesRutaMatchesRolesUsuario(rolesRuta));
    }

    rolesRutaMatchesRolesUsuario(rolesRuta: Rol[]) {
        rolesRuta = rolesRuta || [];
        if (rolesRuta.length === 0) {
            return true;
        }
        if (!this.userIdentity || !this.userIdentity.roles) {
            return false;
        }
        return rolesRuta.filter((rolRuta) => this.userIdentity.roles.filter((rolUsuario) => rolRuta === rolUsuario).length >= 1).length >= 1;

    }

    identity(): Promise<any> {
        // check and see if we have retrieved the userIdentity data from the server.
        // if we have, reuse it by immediately resolving
        if (this.userIdentity) {
            return Promise.resolve(this.userIdentity);
        }

        // retrieve the userIdentity data from the server, update the identity object, and then resolve.
        return this.userService.getLogueado().toPromise().then((account) => {
            if (account) {
                this.userIdentity = account;
                this.authenticated = true;
            } else {
                this.userIdentity = null;
                this.authenticated = false;
            }
            this.authenticationState.next(this.userIdentity);
            return this.userIdentity;
        }).catch((err) => {
            this.userIdentity = null;
            this.authenticated = false;
            this.authenticationState.next(this.userIdentity);
            return null;
        });
    }

    isAuthenticated(): boolean {
        return this.authenticated;
    }

    isIdentityResolved(): boolean {
        return this.userIdentity !== undefined;
    }

    getAuthenticationState(): Observable<any> {
        return this.authenticationState.asObservable();
    }

    public correctlyLogged(): boolean {
        return Boolean(this.userIdentity && this.userIdentity.roles.length !== 0);
    }
}
