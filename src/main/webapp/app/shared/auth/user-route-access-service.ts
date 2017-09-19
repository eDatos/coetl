import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, Data } from '@angular/router';

import { LoginModalService } from '../login/login-modal.service';
import { StateStorageService } from './state-storage.service';
import { ConfigService } from '../../config/index';
import { Operacion } from '../../entities/operacion/index';
import { Principal } from './principal.service';

@Injectable()
export class UserRouteAccessService implements CanActivate {

    public static AUTH_REDIRECT = 'authRedirect';
    private static OPERACIONES = 'operaciones';

    constructor(
        private router: Router,
        private loginModalService: LoginModalService,
        private principal: Principal,
        private stateStorageService: StateStorageService,
        private configService: ConfigService,
    ) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Promise<boolean> {

        const operaciones = this.operacionesFromRoute(route);

        return Promise.resolve(this.checkLogin(operaciones, state.url).then((canActivate) => {
            if (!canActivate) {
                this.redirect(route.data);
            }
            return true;
        }));
    }

    checkLogin(operaciones: Operacion[], url: string): Promise<boolean> {
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

    private operacionesFromRoute(route: ActivatedRouteSnapshot): Operacion[] {
        let operaciones = [];
        if (route.firstChild && route.firstChild.data && route.firstChild.data[UserRouteAccessService.OPERACIONES]) {
            operaciones = route.firstChild.data[UserRouteAccessService.OPERACIONES];
        } else {
            operaciones = route.data[UserRouteAccessService.OPERACIONES];
        }
        if (operaciones && operaciones.length > 0) {
            operaciones = operaciones.map((operacion) => {
                if (typeof operacion === 'string') {
                    const operacionValues = operacion.split('#');
                    operacion = new Operacion(null, operacionValues[0], operacionValues[1]);
                }
                return operacion;
            });
        }
        return operaciones;
    }

    private redirectToCas() {
        const config = this.configService.getConfig();
        window.location.href = config.cas.login + '?service=' + encodeURIComponent(config.cas.applicationHome);
        return false;
    }
}
