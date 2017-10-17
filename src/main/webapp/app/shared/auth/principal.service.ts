import { UserService } from '../../shared/user/user.service';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import { Rol } from '../index';
import { Operacion } from '../../entities/operacion/index';
import { Account } from '../../shared/user/account.model';
import { OperacionService } from '../../entities/operacion/operacion.service';

@Injectable()
export class Principal {
    [x: string]: any;
    private userIdentity: Account;
    private authenticated = false;
    private authenticationState = new Subject<any>();

    constructor(
        private operacionService: OperacionService,
        private userService: UserService,
    ) { }

    authenticate(identity) {
        this.userIdentity = identity;
        this.authenticated = identity !== null;
        this.authenticationState.next(this.userIdentity);
    }

    canDoAnyOperacion(operacionesRuta: Operacion[] | string[]): Promise<boolean> {
        return Promise.resolve(this.operacionesRutaMatchesOperacionesUsuario(operacionesRuta));
    }

    private operacionesRutaMatchesOperacionesUsuario(operacionesRuta: Operacion[] | string[]) {
        let operacionesUsuario: Operacion[] = [];
        operacionesRuta = operacionesRuta || [];
        if (operacionesRuta.length === 0) {
            return true;
        }
        if (!this.userIdentity || !this.userIdentity.roles) {
            return false;
        }
        operacionesUsuario = [].concat.apply([], this.userIdentity.roles.map((r) => r.operaciones))
        return this.operacionService.operacionFromString(operacionesRuta)
            .filter((ou) =>
                operacionesUsuario
                    .filter((or) => this.sameOperacion(or, ou)).length >= 1)
            .length >= 1;

    }

    private sameOperacion(o1: Operacion, o2: Operacion) {
        const result = o1 && o2
            && o1.accion !== undefined && o1.sujeto !== undefined
            && o2.accion !== undefined && o2.sujeto !== undefined
            && o1.accion === o2.accion
            && o1.sujeto === o2.sujeto
        return result;
    }

    hasAnyRol(roles: String[]): Promise<boolean> {
        throw new Error('NOT IMPLEMENTED');
    }

    identity(force?: boolean): Promise<any> {
        if (force === true) {
            this.userIdentity = undefined;
        }

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
        return Boolean(this.userIdentity.roles.length !== 0);
    }
}
