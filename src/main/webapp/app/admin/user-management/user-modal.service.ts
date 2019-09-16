import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { User, UserService } from '../../shared';

@Injectable()
export class UserModalService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private userService: UserService
    ) {}

    open(component: Component, login?: string): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (login) {
            this.userService
                .find(login, true)
                .subscribe((user) => this.userModalRef(component, user));
        } else {
            // Este setTimeout es un fix para evitar el error "ExpressionChangedAfterItHasBeenCheckedError" que aparece.
            // Ver: https://github.com/jhipster/generator-jhipster/issues/5985
            setTimeout(() => {
                this.userModalRef(component, new User());
            }, 0);
        }
    }

    userModalRef(component: Component, user: User): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.user = user;
        modalRef.result.then(
            (result) => {
                this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true });
                this.isOpen = false;
            },
            (reason) => {
                if (reason === 'deleted') {
                    this.router.navigateByUrl('user-management');
                } else {
                    this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true });
                }
                this.isOpen = false;
            }
        );
        return modalRef;
    }
}
