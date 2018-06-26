import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, Data, Route } from '@angular/router';

import { StateStorageService } from './state-storage.service';
import { ConfigService } from '../../config/index';
import { Principal } from './principal.service';
import { Rol } from '../rol/rol.model';
import { RolService } from '../rol/rol.service';

@Injectable()
export class UserRouteAccessService implements CanActivate {

    public static AUTH_REDIRECT = 'authRedirect';
    private static ROLES = 'roles';

    constructor(
        private router: Router,
        private principal: Principal,
        private configService: ConfigService,
        private rolService: RolService
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

    checkLogin(roles: Rol[]): Promise<boolean> {
        const principal = this.principal;
        return Promise.resolve(principal.identity().then((account) => {
            if (!!account) { // User is logged in
                // FIXME INFRASTR-61 poner roles aquí.
                return this.noPermissionRequired(roles) || principal.hasRoles(roles);
            } else { // User is not logged in, redirect to CAS
                this.redirectToCas();
            }
        }));
    }

    private noPermissionRequired(roles: Rol[]) {
        return (!roles || roles.length === 0)
    }

    private redirect(data: Data) {
        if (data[UserRouteAccessService.AUTH_REDIRECT]) {
            this.router.navigate([data[UserRouteAccessService.AUTH_REDIRECT]]);
        } else {
            this.router.navigate(['accessdenied']);
        }
    }

    private operacionesFromRouteSnapshot(route: ActivatedRouteSnapshot): Rol[] {
        if (route.firstChild && route.firstChild.data && route.firstChild.data[UserRouteAccessService.ROLES]) {
            return this.operacionesFromRoute(route.firstChild);
        } else {
            return this.operacionesFromRoute(route);
        }
    }

    public operacionesFromRoute(route): Rol[] {
        return this.rolService.rolFromString(route.data[UserRouteAccessService.ROLES]);
    }

    public redirectToCas() {
        const config = this.configService.getConfig();
        window.location.href = config.cas.login + '?service=' + encodeURIComponent(config.cas.applicationHome);
        return false;
    }
}
