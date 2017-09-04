import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { JhiEventManager } from 'ng-jhipster';

import { UserModalService } from './user-modal.service';
import { JhiLanguageHelper, User, UserService, RolService, Rol } from '../../shared';
import { Subscription } from 'rxjs/Rx';

@Component({
    selector: 'jhi-user-mgmt-dialog',
    templateUrl: './user-management-dialog.component.html'
})
export class UserMgmtDialogComponent implements OnInit, OnDestroy {

    user: User;
    languages: any[];
    roles: any[];
    isSaving: Boolean;
    usuarioValido = false;
    private subscription: Subscription;

    constructor(
        private languageHelper: JhiLanguageHelper,
        private userService: UserService,
        private eventManager: JhiEventManager,
        private rolService: RolService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit() {
        this.isSaving = false;
        this.roles = [];
        this.rolService.roles().subscribe((roles) => {
            this.roles = roles;
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
            this.userService.find(login).subscribe((user) => {
                this.user = user;
                this.userService.buscarUsuarioEnLdap(this.user.login).subscribe((usuarioLdap) => {
                    if (!!usuarioLdap) {
                        this.usuarioValido = true;
                    }
                });
            });
        } else {
            this.user = new User();
        }
    }

    clear() {
        // const with arrays: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/const
        const returnPath = ['user-management'];
        if (this.user.login) {
            returnPath.push(this.user.login);
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

    validarUsuario() {
        this.userService.buscarUsuarioEnLdap(this.user.login).subscribe(
            (usuario) => {
                this.user = usuario;
                this.usuarioValido = true;
            },
            (error) => {
                this.usuarioValido = false;
            }
        );
    }

    compareRoles(r1: Rol, r2: Rol): boolean {
        return r1 && r2 ? r1.nombre === r2.nombre : r1 === r2;
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
