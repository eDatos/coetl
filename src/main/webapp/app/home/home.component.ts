import { Component, OnInit } from '@angular/core';

import { Router, Data } from '@angular/router';
import { UserRouteAccessService, Principal, Account } from '../shared';
import { DEFAULT_ROUTE } from '../admin/user-management/user-management.route';

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
        this.userRouteAccessService.checkLogin(this.userRouteAccessService.operacionesFromRoute(DEFAULT_ROUTE)).then((canActivate) => {
            if (canActivate) {
                this.router.navigate([DEFAULT_ROUTE.path]);
            }
        });
        this.principal.identity().then((account) => {
            this.account = account;
        });
    }

    getNombreCompleto(account: Account): string {
        return [account.nombre, account.apellido1, account.apellido2].join(' ');
    }
}
