import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { UserService } from '../../shared';

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
        private userService: UserService,
        private route: ActivatedRoute,
        private router: Router
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.userService
            .getLogueado()
            .toPromise()
            .then((account) => {
                this.settingsAccount = account;
            });
    }

    save() {
        this.isSaving = true;
        this.userService.update(this.settingsAccount).subscribe(
            () => {
                this.error = null;
                this.success = 'OK';
                this.isSaving = false;
                this.router.navigate(['settings']);
            },
            () => {
                this.success = null;
                this.error = 'ERROR';
                this.isSaving = false;
            }
        );
    }

    clear() {
        // const with arrays: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/const
        const returnPath = ['settings'];
        this.router.navigate(returnPath);
    }

    isEditMode(): Boolean {
        const lastPath = this.route.snapshot.url[this.route.snapshot.url.length - 1].path;
        return lastPath === 'edit';
    }
}
