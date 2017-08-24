import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';

import { Principal } from '../';
import { LoginModalService } from '../login/login-modal.service';
import { StateStorageService } from './state-storage.service';
import { ConfigService } from '../../config/index';

@Injectable()
export class UserRouteAccessService implements CanActivate {

    constructor(private router: Router,
        private loginModalService: LoginModalService,
        private principal: Principal,
        private stateStorageService: StateStorageService,
        private configService: ConfigService,
    ) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Promise<boolean> {

        const authorities = route.data['authorities'];
        if (!authorities || authorities.length === 0) {
            return true;
        }

        return this.checkLogin(authorities, state.url);
    }

    checkLogin(authorities: string[], url: string): Promise<boolean> {
        const principal = this.principal;
        return Promise.resolve(principal.identity().then((account) => {

            if (account && principal.hasAnyAuthorityDirect(authorities)) {
                return true;
            }

            this.stateStorageService.storeUrl(url);
            const config = this.configService.getConfig();
            window.location.href = config.cas.login + '?service=' + encodeURIComponent(config.cas.applicationHome);
            return false;
        }));
    }
}
