import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import { AccountService } from './account.service';
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
        private account: AccountService,
        private operacionService: OperacionService,
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
        const operacionesUsuario: Operacion[] = this.userIdentity.roles
            .map((r) => r.operaciones)
            .reduce(([], operacion) => operacion);
        operacionesRuta = this.operacionService.operacionFromString(operacionesRuta);
        // TODO CORREGIR COMPROBACIONES DE RUTA
        return operacionesRuta
            .filter((or) => operacionesUsuario
                .filter((ou) => this.sameOperacion(or, ou)))
            .length !== 0;
    }

    private sameOperacion(o1: Operacion, o2: Operacion) {
        return o1 && o2
            && o1.accion === o2.accion
            && o1.sujeto === o2.sujeto
            ;
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
        return this.account.get().toPromise().then((account) => {
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

    getImageUrl(): String {
        return this.isIdentityResolved() ? this.userIdentity.urlImage : null;
    }
}
