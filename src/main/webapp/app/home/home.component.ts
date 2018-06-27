import { errorRoute } from '../layouts/error/error.route';
import { Component, OnInit } from '@angular/core';

import { Router, Data } from '@angular/router';
import { UserRouteAccessService, Principal, Account, Rol } from '../shared';

/**
 * FIXME
 * La constante DEFAULT_PATH debe contener el path de primer nivel al que los usuarios acceden al entrar a la aplicación.
 * La constante DEFAULT_OPERACIONES debe contener las operaciones que debe tener el usuario que quiere acceder a esta página por defecto
 * Estas constantes deben ser luego usadas en el route en cuestión del componente al que corresponden
 **/
export const DEFAULT_PATH = 'user-management';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {

    public account: Account;

    constructor(
        private principal: Principal,
        private userRouteAccessService: UserRouteAccessService,
        private router: Router
    ) { }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;

            if (!account.id && account.roles.length === 0) {
                this.router.navigate(['non-existent-user']);
            }

            if (account.deletionDate) {
                this.router.navigate(['blocked']);
            }
            this.userRouteAccessService.checkLogin([Rol.ADMIN]).then((canActivate) => {
                if (canActivate) {
                    this.router.navigate([DEFAULT_PATH]);
                }
            });
        });
    }

    getNombreCompleto(account: Account): string {
        return [account.nombre, account.apellido1, account.apellido2].join(' ');
    }
}
