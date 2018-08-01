import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Response } from '@angular/http';
import { JhiEventManager } from 'ng-jhipster';
import { User, UserService, Rol } from '../../shared';
import { Subscription } from 'rxjs';
import { PermissionService } from '../../shared';

@Component({
    selector: 'jhi-user-mgmt-form',
    templateUrl: './user-management-form.component.html'
})
export class UserMgmtFormComponent implements OnInit, OnDestroy {
    user: User;
    isSaving: Boolean;
    usuarioValido = false;
    roleEnum = Rol;
    roleSelected: Rol;
    private subscription: Subscription;
    paramLogin: string;
    eventSubscriber: Subscription;

    constructor(
        private userService: UserService,
        public permissionService: PermissionService,
        private eventManager: JhiEventManager,
        private route: ActivatedRoute,
        private router: Router
    ) {}

    ngOnInit() {
        this.isSaving = false;

        this.subscription = this.route.params.subscribe((params) => {
            this.paramLogin = params['login'];
            this.load(this.paramLogin);
        });
        this.eventSubscriber = this.eventManager.subscribe('UserModified', (response) => {
            if (!response.content || response.content.action !== 'deleted') {
                this.load(response.content);
            }
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
                this.roleSelected = this.user.roles[0];
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
        if (this.paramLogin) {
            returnPath.push(this.paramLogin);
        }
        this.router.navigate(returnPath);
    }

    save() {
        this.isSaving = true;
        this.user.roles[0] = this.roleSelected;
        if (this.user.id !== null) {
            this.userService
                .update(this.user)
                .subscribe((response) => this.onSaveSuccess(response), () => this.onSaveError());
        } else {
            this.userService
                .create(this.user)
                .subscribe((response) => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }

    validarUsuario(inputDirty = true) {
        if (inputDirty) {
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
    }

    restore(login: string) {
        this.userService.restore(login).subscribe((res: Response) => {
            this.eventManager.broadcast({
                name: 'UserModified',
                content: login
            });
        });
    }

    private onSaveSuccess(result) {
        this.eventManager.broadcast({ name: 'userListModification', content: 'OK' });
        this.isSaving = false;
        this.router.navigate(['user-management', this.user.login]);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }
}
