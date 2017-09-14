import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, Data } from '@angular/router';

import { Principal, Rol } from '../';
import { LoginModalService } from '../login/login-modal.service';
import { StateStorageService } from './state-storage.service';
import { ConfigService } from '../../config/index';
import { Operacion } from '../../entities/operacion/index';

@Injectable()
export class UserRouteAccessService implements CanActivate {

    public static AUTH_REDIRECT = 'authRedirect';

    constructor(private router: Router,
        private loginModalService: LoginModalService,
        private principal: Principal,
        private stateStorageService: StateStorageService,
        private configService: ConfigService,
    ) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Promise<boolean> {

        const roles = route.data['roles'];
        const operaciones = route.data['operaciones'];

        if (this.noPermissionRequired(roles, operaciones)) {
            return true;
        }

        return Promise.resolve(this.checkLogin(roles, operaciones, state.url).then((canActivate) => {
            if (canActivate) {
                return true;
            } else {
                this.redirect(route.data);
            }
        }));

    }

    checkLogin(roles: String[], operaciones: Operacion[], url: string): Promise<boolean> {
        const principal = this.principal;
        return Promise.resolve(principal.identity().then((account) => {

            // Access using roles
            if (account && roles && principal.hasAnyRolDirect(roles)) {
                return true;
            }

            // Access using operations
            if (account && operaciones && principal.canDoAnyOperacion(operaciones)) {
                return true;
            }

            // User is logged in, but has no permissions
            if (account) {
                return false;
            }

            // User is not logged in, redirect to CAS
            const config = this.configService.getConfig();
            window.location.href = config.cas.login + '?service=' + encodeURIComponent(config.cas.applicationHome);
            return false;
        }));
    }

    private noPermissionRequired(roles: string[], operaciones: Operacion[]) {
        return (!roles || roles.length === 0) && (!operaciones || operaciones.length === 0)
    }

    private redirect(data: Data) {
        if (data[UserRouteAccessService.AUTH_REDIRECT]) {
            this.router.navigate([data[UserRouteAccessService.AUTH_REDIRECT]]);
        } else {
            this.router.navigate([UserRouteAccessService.AUTH_REDIRECT]);
        }
    }
}
