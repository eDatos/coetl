import { Injectable } from '@angular/core';

import { Principal } from './principal.service';
import { AuthServerProvider } from './auth-jwt.service';
import { ConfigService } from '../../../config';

@Injectable()
export class LoginService {
    constructor(
        private principal: Principal,
        private authServerProvider: AuthServerProvider,
        private configService: ConfigService
    ) {}

    logout() {
        this.authServerProvider.logout().subscribe();
        this.principal.authenticate(null);
    }

    login() {
        const config = this.configService.getConfig();
        window.location.href =
            config.cas.login + '?service=' + encodeURIComponent(config.cas.service);
    }
}
