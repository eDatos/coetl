import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import { UserCAS } from '../user/account.model';
import { Rol } from './rol.model';
import { AuthServerProvider } from './auth-jwt.service';
import { ExternalItem } from '../../../entities/external-item';

@Injectable()
export class Principal {
    [x: string]: any;
    private userIdentity: UserCAS;
    private authenticated = false;
    private authenticationState = new Subject<any>();

    constructor(private authServerProvider: AuthServerProvider) {}

    authenticate(identity) {
        this.userIdentity = identity;
        this.authenticated = identity !== null;
        this.authenticationState.next(this.userIdentity);
    }

    hasRoles(rolesRuta: Rol[]): Promise<boolean> {
        return Promise.resolve(this.rolesRutaMatchesRolesUsuario(rolesRuta));
    }

    rolesRutaMatchesRolesUsuario(rolesRuta: Rol[], operation?: ExternalItem) {
        // Rol is determined by the statistical operation associated to ETL.
        rolesRuta = rolesRuta || [];
        if (rolesRuta.length === 0) {
            return true;
        }
        if (!this.userIdentity || !this.userIdentity.roles) {
            return false;
        }
        return (
            rolesRuta.filter((rolRuta) => this.userIdentity.hasRole(rolRuta, operation)).length >= 1
        );
    }

    identity(): Promise<any> {
        // check and see if we have retrieved the userIdentity data from the server.
        // if we have, reuse it by immediately resolving
        if (this.userIdentity) {
            return Promise.resolve(this.userIdentity);
        }

        const token: string = this.authServerProvider.getToken();
        if (token) {
            this.userIdentity = UserCAS.fromJwt(token);
            this.authenticated = true;
        } else {
            this.userIdentity = null;
            this.authenticated = false;
        }

        this.authenticationState.next(this.userIdentity);
        return Promise.resolve(this.userIdentity);
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
