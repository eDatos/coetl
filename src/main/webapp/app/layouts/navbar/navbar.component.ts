import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import {
    ProfileService,
    Principal,
    LoginService,
    PermissionService,
    InstallationService
} from '../../shared';
import { VERSION } from '../../app.constants';
import { ConfigService } from '../../config';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'jhi-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['navbar.component.scss']
})
export class NavbarComponent implements OnInit {
    inProduction: boolean;
    isNavbarCollapsed: boolean;
    modalRef: NgbModalRef;
    version: string;
    isInternalApp: boolean;
    title: string;

    constructor(
        private loginService: LoginService,
        public permissionService: PermissionService,
        private principal: Principal,
        private profileService: ProfileService,
        private configService: ConfigService,
        private translateService: TranslateService,
        private internalInstallationService: InstallationService
    ) {
        this.version = VERSION ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
    }

    ngOnInit() {
        this.profileService.getProfileInfo().subscribe((profileInfo) => {
            this.inProduction = profileInfo.inProduction;
        });
        this.isInternalApp = this.internalInstallationService.isInternalType();
        this.title = this.isInternalApp
            ? this.translateService.instant('global.internalTitle')
            : this.translateService.instant('global.title');
    }

    collapseNavbar() {
        this.isNavbarCollapsed = true;
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    logout() {
        this.collapseNavbar();
        this.loginService.logout();
        const config = this.configService.getConfig();
        window.location.href = config.cas.logout;
    }

    toggleNavbar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
    }

    public correctlyLogged(): boolean {
        return Boolean(this.principal.correctlyLogged());
    }
}
