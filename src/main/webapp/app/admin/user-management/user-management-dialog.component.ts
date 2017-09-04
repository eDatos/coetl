import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { JhiEventManager } from 'ng-jhipster';

import { UserModalService } from './user-modal.service';
import { JhiLanguageHelper, User, UserService } from '../../shared';
import { Subscription } from 'rxjs/Rx';

@Component({
    selector: 'jhi-user-mgmt-dialog',
    templateUrl: './user-management-dialog.component.html'
})
export class UserMgmtDialogComponent implements OnInit, OnDestroy {

    user: User;
    languages: any[];
    authorities: any[];
    isSaving: Boolean;
    private subscription: Subscription;

    constructor(
        private languageHelper: JhiLanguageHelper,
        private userService: UserService,
        private eventManager: JhiEventManager,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = [];
        this.userService.authorities().subscribe((authorities) => {
            this.authorities = authorities;
        });

        this.languageHelper.getAll().then((languages) => {
            this.languages = languages;
        });

        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['login']);
        });
    }

    isEditMode(): Boolean {
        const lastPath = this.route.snapshot.url[this.route.snapshot.url.length - 1].path;
        return lastPath === 'edit' || lastPath === 'user-management-new';
    }

    load(login) {
        if (login) {
            this.userService.find(login).subscribe((user) => this.user = user);
        } else {
            this.user = new User();
        }
    }

    clear() {
        this.router.navigate(['user-management', this.user.login]);
    }

    save() {
        this.isSaving = true;
        if (this.user.id !== null) {
            this.userService.update(this.user).subscribe((response) => this.onSaveSuccess(response), () => this.onSaveError());
        } else {
            this.userService.create(this.user).subscribe((response) => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }

    private onSaveSuccess(result) {
        this.eventManager.broadcast({ name: 'userListModification', content: 'OK' });
        this.isSaving = false;
        this.router.navigate(['user-management']);
    }

    private onSaveError() {
        this.isSaving = false;
    }
    ngOnDestroy() {
        this.subscription.unsubscribe();
    }
}
