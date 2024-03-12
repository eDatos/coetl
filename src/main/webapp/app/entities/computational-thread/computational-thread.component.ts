import { Component, OnInit, OnDestroy } from '@angular/core';
import { PermissionService } from '../../shared';

@Component({
    selector: 'ac-computational-thread',
    templateUrl: './computational-thread.component.html'
})
export class ComputationalThreadComponent implements OnInit, OnDestroy {
    constructor(private permissionService: PermissionService) {}

    ngOnInit() {}

    ngOnDestroy() {}

    public canCreateComputationalThread(): boolean {
        return this.permissionService.canManageEtl();
    }
}
