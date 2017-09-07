import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { JhiEventManager } from 'ng-jhipster';

import { UserModalService } from './user-modal.service';
import { JhiLanguageHelper, User, UserService } from '../../shared';
import { Subscription } from 'rxjs/Rx';

import { AutoComplete } from 'primeng/primeng';

@Component({
    selector: 'jhi-user-mgmt-dialog',
    templateUrl: './user-management-dialog.component.html'
})
export class UserMgmtDialogComponent implements OnInit, OnDestroy {

    user: User;
    languages: any[];
    authorities: any[];
    filteredAuthorities: any[];
    isSaving: Boolean;
    private subscription: Subscription;
    paramLogin: string;

    @ViewChild(AutoComplete) private autoComplete: AutoComplete;

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
            this.filteredAuthorities = this.buildAuthoritiesAutocomplete('');
        });

        this.languageHelper.getAll().then((languages) => {
            this.languages = languages;
        });

        this.subscription = this.route.params.subscribe((params) => {
            this.paramLogin = params['login'];
            this.load(this.paramLogin);
        });
    }

    buildAuthoritiesAutocomplete(query) {
        return this.authorities
            .filter((authority) => authority.toUpperCase().indexOf(query.toUpperCase()) !== -1);
    }

    searchAuthorities(event) {
        this.filteredAuthorities = this.buildAuthoritiesAutocomplete(event.query);
    }

    handleDropdownAuthorities(event) {
        event.originalEvent.preventDefault();
        event.originalEvent.stopPropagation();
        if (this.autoComplete.panelVisible) {
            this.autoComplete.hide();
        } else {
            this.autoComplete.show();
        }
        this.filteredAuthorities = this.buildAuthoritiesAutocomplete('');
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
        // const with arrays: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/const
        const returnPath = ['user-management'];
        if (this.paramLogin) {
            returnPath.push(this.paramLogin);
        }
        this.router.navigate(returnPath);
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
