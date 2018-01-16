import { Component, OnDestroy } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Subscription } from 'rxjs/Rx';

@Component({
    selector: 'jhi-alert-error',
    template: `
        <div *ngIf="alertService.isToast() && alerts.length > 0" class="alert-backdrop"></div>
        <div class="alerts" role="alert" [ngClass]="{\'toast\':alertService.isToast()}">
            <div *ngFor="let alert of alerts" [class]="alert.position">
                <ngb-alert *ngIf="alert && alert.type && alert.msg" [type]="alert.type" (close)="alert.close(alerts)" (click)="alert.close(alerts)">
                    <pre [innerHTML]="alert.msg"></pre>
                </ngb-alert>
            </div>
        </div>`
})
export class JhiAlertErrorComponent implements OnDestroy {

    alerts: any[];
    cleanHttpErrorListener: Subscription;

    // FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)
    constructor(public alertService: JhiAlertService, private eventManager: JhiEventManager, private translateService: TranslateService) {
        this.alerts = [];

        this.cleanHttpErrorListener = eventManager.subscribe('arteApplicationTemplateApp.httpError', (response) => {
            let i;
            const httpResponse = response.content;
            switch (httpResponse.status) {
                // connection refused, server not reachable
                case 0:
                    this.addErrorAlert('Server not reachable', 'error.server.not.reachable');
                    break;

                case 400:
                case 404:
                    const arr = Array.from(httpResponse.headers._headers);
                    const headers = [];
                    for (i = 0; i < arr.length; i++) {
                        if (arr[i][0].endsWith('app-error') || arr[i][0].endsWith('app-params')) {
                            headers.push(arr[i][0]);
                        }
                    }
                    headers.sort();
                    let errorHeader = null;
                    let entityKey = null;
                    if (headers.length > 1) {
                        errorHeader = httpResponse.headers.get(headers[0]);
                        entityKey = httpResponse.headers.get(headers[1]);
                    }
                    if (errorHeader) {
                        const entityName = translateService.instant('global.menu.entities.' + entityKey);
                        this.addErrorAlert(errorHeader, errorHeader, { entityName });
                    } else if (httpResponse.text() !== '' && httpResponse.json() && httpResponse.json().fieldErrors) {
                        const fieldErrors = httpResponse.json().fieldErrors;
                        for (i = 0; i < fieldErrors.length; i++) {
                            const fieldError = fieldErrors[i];
                            // convert 'something[14].other[4].id' to 'something[].other[].id' so translations can be written to it
                            const convertedField = fieldError.field.replace(/\[\d*\]/g, '[]');
                            const fieldName = translateService.instant('arteApplicationTemplateApp.' +
                                fieldError.objectName + '.' + convertedField);
                            this.addErrorAlert(
                                'Error on field "' + fieldName + '"', 'error.' + fieldError.message, { fieldName });
                        }
                    } else if (httpResponse.text() !== '' && httpResponse.json() && httpResponse.json().message) {
                        this.addErrorAlert(httpResponse.json().message, httpResponse.json().message, httpResponse.json().params);
                    } else if (httpResponse.text()) {
                        this.addErrorAlert(httpResponse.text());
                    } else {
                        this.addErrorAlert('Not found', 'error.url.not.found');

                    }
                    break;
                default:
                    if (httpResponse.text() !== '' && httpResponse.json() && httpResponse.json().message) {
                        this.addErrorAlert(httpResponse.json().message);
                    } else {
                        this.addErrorAlert(httpResponse.text());
                    }
            }
        });
    }

    ngOnDestroy() {
        if (this.cleanHttpErrorListener !== undefined && this.cleanHttpErrorListener !== null) {
            this.eventManager.destroy(this.cleanHttpErrorListener);
            this.alerts = [];
        }
    }

    addErrorAlert(message, key?, data?) {
        key = (key && key !== null) ? key : message;
        this.alerts.push(
            this.alertService.addAlert(
                {
                    type: 'danger',
                    msg: key,
                    params: data,
                    timeout: 0,
                    toast: this.alertService.isToast(),
                    scoped: true,
                    position: 'top'
                },
                this.alerts
            )
        );
    }
}
