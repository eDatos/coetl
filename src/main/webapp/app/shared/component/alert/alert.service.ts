import { Injectable } from '@angular/core';
import { JhiAlert, JhiAlertService } from 'ng-jhipster';

@Injectable()
export class AcAlertService {
    constructor(private alertService: JhiAlertService) {}

    // No traduce el message recibido, al contrario que hace el alertService.error
    public error(message: string) {
        const id = this.alertService.get().length + 1;

        const alert: JhiAlert = {
            type: 'danger',
            msg: message,
            id,
            timeout: 0,
            toast: this.alertService.isToast(),
            position: 'top right',
            scoped: undefined,
            close: (alerts) => this.alertService.closeAlert(id, alerts)
        };

        this.alertService.get().push(alert);
        return alert;
    }
}
