import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';

import { RolService, Rol } from '../../shared';

@Component({
    selector: 'jhi-rol-mgmt-detail',
    templateUrl: './rol-management-detail.component.html'
})
export class RolMgmtDetailComponent implements OnInit, OnDestroy {

    rol: Rol;
    private subscription: Subscription;

    constructor(
        private rolService: RolService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['nombre']);
        });
    }

    load(nombre) {
        this.rolService.find(nombre).subscribe((rol) => {
            this.rol = rol;
        });
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
