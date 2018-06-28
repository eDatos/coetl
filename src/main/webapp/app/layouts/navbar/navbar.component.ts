import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { ProfileService } from '../profiles/profile.service';
import { Principal, LoginService } from '../../shared';

import { VERSION } from '../../app.constants';
import { ConfigService } from '../../config/index';
import { AdminPermissionService } from '../../admin/admin-permission.service';
import { ActorPermissionService } from '../../entities/actor';
import { PeliculaPermissionService } from '../../entities/pelicula';

@Component({
    selector: 'jhi-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: [
        'navbar.component.scss'
    ]
})
export class NavbarComponent implements OnInit {

    inProduction: boolean;
    isNavbarCollapsed: boolean;
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    version: string;

    constructor(
        private loginService: LoginService,
        public adminPermissionService: AdminPermissionService,
        public actorPermissionService: ActorPermissionService,
        public peliculaPermissionService: PeliculaPermissionService,
        private principal: Principal,
        private profileService: ProfileService,
        private configService: ConfigService,
    ) {
        this.version = VERSION ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
    }

    ngOnInit() {
        this.profileService.getProfileInfo().subscribe((profileInfo) => {
            this.inProduction = profileInfo.inProduction;
            this.swaggerEnabled = profileInfo.swaggerEnabled;
        });
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

    isVideotecaVisible(): boolean {
        return this.peliculaPermissionService.puedeNavegarPelicula() || this.actorPermissionService.puedeNavegarActor();
    }
}
