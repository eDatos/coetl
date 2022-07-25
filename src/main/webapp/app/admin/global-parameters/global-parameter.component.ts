import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import { GenericModalService, ResponseWrapper } from '../../shared';
import { Parameter, Typology } from '../../entities/parameter';

import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { GloablParameterService } from './global-parameter.service';
import { GlobalParameterDeleteDialogComponent } from './global-parameter-dialog/global-parameter-delete-dialog.component';
import { GlobalParameterDialogComponent } from './global-parameter-dialog/global-parameter-dialog.component';

@Component({
    templateUrl: 'global-parameter.component.html',
    styleUrls: ['global-parameter.component.scss']
})
export class GlobalParameterComponent implements OnInit, OnDestroy {
    public static EVENT_NAME = 'etlGlobalParameterListModification';
    private page: number;
    private totalItems: number;
    private itemsPerPage: number;

    public isPassword: boolean;
    public parameters: Parameter[];

    private routeDataSubscription: any;
    private predicate: any;
    private reverse: any;

    private eventSubscriber: Subscription;

    constructor(
        private globalParameterService: GloablParameterService,
        private activatedRoute: ActivatedRoute,
        private genericModalService: GenericModalService,
        private translateService: TranslateService,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.routeDataSubscription = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
            this.itemsPerPage = data['pagingParams'].itemsPerPage;
        });
    }

    ngOnInit() {
        this.loadAll();
        this.registerChanges();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    private loadAll(req?: { page; size }) {
        const requestOption = req
            ? req
            : {
                  page: this.page,
                  size: this.itemsPerPage
              };

        this.globalParameterService
            .findAllParameters({
                page: requestOption.page - 1,
                size: requestOption.size,
                sort: this.sort()
            })
            .subscribe((response: ResponseWrapper) =>
                this.onSuccess(response.json, response.headers)
            );
    }

    private registerChanges() {
        this.eventSubscriber = this.eventManager.subscribe(
            GlobalParameterComponent.EVENT_NAME,
            (response) => this.loadAll()
        );
    }

    public transition() {
        this.router.navigate(['/global-parameters'], {
            queryParams: Object.assign({}, this.activatedRoute.snapshot.queryParams, {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            })
        });
    }

    private sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    public existParameters(): boolean {
        return !!this.parameters && !!this.parameters.length;
    }

    public editParameter(parameter?: Parameter) {
        let copy = new Parameter();
        if (!!parameter) {
            copy = Object.assign(copy, parameter);
            if (Typology.PASSWORD === parameter.typology) {
                this.globalParameterService.decodeParameter(parameter.id).subscribe((response) => {
                    this.openEditParameterDialog(response);
                });
            } else {
                this.openEditParameterDialog(copy);
            }
        } else {
            this.openEditParameterDialog(copy);
        }
    }

    private openEditParameterDialog(parameter: Parameter) {
        this.genericModalService.open(
            GlobalParameterDialogComponent as Component,
            { parameter },
            { container: '.app' }
        );
    }

    public deleteParameter(parameter: Parameter) {
        const copy = Object.assign(new Parameter(), parameter);

        this.genericModalService.open(
            GlobalParameterDeleteDialogComponent as Component,
            { parameter: copy },
            { container: '.app' }
        );
    }

    private onSuccess(data: Parameter[], headers) {
        this.totalItems = headers.get('X-Total-Count');
        this.parameters = data;
    }

    public isPasswordTypology(parameter: Parameter): boolean {
        return parameter.typology === Typology.PASSWORD ? true : false;
    }

    public getTypeName(parameter: Parameter): string {
        return this.translateService.instant(`coetlApp.parameter.type.${parameter.type}`);
    }
}
