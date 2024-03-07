import { Component, OnInit, OnDestroy } from '@angular/core';
import { PermissionService } from '../../../shared';

@Component({
    selector: 'ac-computational-thread-form',
    templateUrl: 'computational-thread-form.component.html'
})
export class ComputationalThreadFormComponent implements OnInit, OnDestroy {
    public static EVENT_NAME = 'computationalThreadFormEvent';

    constructor(private permissionService: PermissionService) {}

    ngOnInit() {}

    ngOnDestroy() {}

    public canCreateComputationalThread(): boolean {
        return this.permissionService.canManageEtl();
    }
}
