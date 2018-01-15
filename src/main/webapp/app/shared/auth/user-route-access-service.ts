import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, Data, Route } from '@angular/router';

import { StateStorageService } from './state-storage.service';
import { ConfigService } from '../../config/index';
import { Operacion } from '../../entities/operacion/index';
import { Principal } from './principal.service';
import { OperacionService } from '../../entities/operacion/operacion.service';

@Injectable()
export class UserRouteAccessService implements CanActivate {

    public static AUTH_REDIRECT = 'authRedirect';
    private static OPERACIONES = 'operaciones';

    constructor(
        private router: Router,
        private principal: Principal,
        private stateStorageService: StateStorageService,
        private configService: ConfigService,
        private operacionService: OperacionService,
    ) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Promise<boolean> {

        const operaciones = this.operacionesFromRouteSnapshot(route);

        return Promise.resolve(this.checkLogin(operaciones).then((canActivate) => {
            if (!canActivate) {
                this.redirect(route.data);
            }
            return true;
        }));
    }

    checkLogin(operaciones: Operacion[]): Promise<boolean> {
        const principal = this.principal;
        return Promise.resolve(principal.identity().then((account) => {
            if (!!account) { // User is logged in
                return this.noPermissionRequired(operaciones) || principal.canDoAnyOperacion(operaciones);
            } else { // User is not logged in, redirect to CAS
                this.redirectToCas();
            }
        }));
    }

    private noPermissionRequired(operaciones: Operacion[]) {
        return (!operaciones || operaciones.length === 0)
    }

    private redirect(data: Data) {
        if (data[UserRouteAccessService.AUTH_REDIRECT]) {
            this.router.navigate([data[UserRouteAccessService.AUTH_REDIRECT]]);
        } else {
            this.router.navigate(['accessdenied']);
        }
    }

    private operacionesFromRouteSnapshot(route: ActivatedRouteSnapshot): Operacion[] {
        if (route.firstChild && route.firstChild.data && route.firstChild.data[UserRouteAccessService.OPERACIONES]) {
            return this.operacionesFromRoute(route.firstChild);
        } else {
            return this.operacionesFromRoute(route);
        }
    }

    public operacionesFromRoute(route): Operacion[] {
        return this.operacionService.operacionFromString(route.data[UserRouteAccessService.OPERACIONES]);
    }

    private redirectToCas() {
        const config = this.configService.getConfig();
        window.location.href = config.cas.login + '?service=' + encodeURIComponent(config.cas.applicationHome);
        return false;
    }
}
