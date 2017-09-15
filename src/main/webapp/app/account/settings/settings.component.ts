import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { Principal, AccountService } from '../../shared';

@Component({
    selector: 'jhi-settings',
    templateUrl: './settings.component.html'
})
export class SettingsComponent implements OnInit {
    error: string;
    success: string;
    isSaving: Boolean;
    settingsAccount: any;

    constructor(
        private account: AccountService,
        private route: ActivatedRoute,
        private router: Router,
        private principal: Principal
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.principal.identity().then((account) => {
            this.settingsAccount = this.copyAccount(account);
        });
    }

    save() {
        this.isSaving = true;
        this.account.save(this.settingsAccount).subscribe(() => {
            this.error = null;
            this.success = 'OK';
            this.isSaving = false;
            this.principal.identity(true).then((account) => {
                this.settingsAccount = this.copyAccount(account);
            });
            this.router.navigate(['settings']);
        }, () => {
            this.success = null;
            this.error = 'ERROR';
            this.isSaving = false;
        });
    }

    clear() {
        // const with arrays: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/const
        const returnPath = ['settings'];
        // if (this.paramLogin) {
        //     returnPath.push(this.paramLogin);
        // }
        this.router.navigate(returnPath);
    }

    copyAccount(account) {
        return {
            activado: account.activado,
            email: account.email,
            nombre: account.nombre,
            idioma: account.idioma,
            apellido1: account.apellido1,
            apellido2: account.apellido2,
            login: account.login,
            urlImagen: account.urlImagen
        };
    }

    isEditMode(): Boolean {
        const lastPath = this.route.snapshot.url[this.route.snapshot.url.length - 1].path;
        return lastPath === 'edit';
    }
}
