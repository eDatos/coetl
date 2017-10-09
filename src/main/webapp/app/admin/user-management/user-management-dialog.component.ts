import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Response } from '@angular/http';

import { JhiEventManager } from 'ng-jhipster';

import { UserModalService } from './user-modal.service';
import { User, UserService, RolService, Rol } from '../../shared';
import { Subscription } from 'rxjs/Rx';

@Component({
    selector: 'jhi-user-mgmt-dialog',
    templateUrl: './user-management-dialog.component.html'
})
export class UserMgmtDialogComponent implements OnInit, OnDestroy {

    user: User;
    authorities: any[];
    isSaving: Boolean;
    usuarioValido = false;
    private subscription: Subscription;
    paramLogin: string;
    eventSubscriber: Subscription;

    constructor(
        private userService: UserService,
        private eventManager: JhiEventManager,
        private rolService: RolService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = [];
        this.rolService.query().subscribe((roles) => {
            this.authorities = roles.json.map((rol: any) => rol);
        });

        this.subscription = this.route.params.subscribe((params) => {
            this.paramLogin = params['login'];
            this.load(this.paramLogin);
        });
        this.eventSubscriber = this.eventManager.subscribe('UserModified', (response) => {
            if (!response.content || response.content.action !== 'deleted') {
                this.load(response.content)
            }
        });
    }

    rolItemTemplate(item: Rol) {
        return item.nombre;
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
        this.userService.restore(login)
            .subscribe((res: Response) => {
                this.eventManager.broadcast(
                    {
                        name: 'UserModified',
                        content: login
                    });
            })
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
