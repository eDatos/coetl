import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { JhiParseLinks } from 'ng-jhipster';
import { ActivatedRoute, Router } from '@angular/router';

import { Audit } from './audit.model';
import { AuditsService } from './audits.service';
import { ITEMS_PER_PAGE } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

import { Validators, FormControl, FormGroup } from '@angular/forms';

@Component({
    selector: 'jhi-audit',
    templateUrl: './audits.component.html',
    styleUrls: ['audits.component.scss']
})
export class AuditsComponent implements OnInit {
    audits: Audit[];
    fromDate: Date;
    itemsPerPage: any;
    routeData: any;
    links: any;
    page: number;
    orderProp: string;
    reverse: boolean;
    predicate: any;
    toDate: Date;
    totalItems: number;
    today: Date;

    constructor(
        private auditsService: AuditsService,
        private parseLinks: JhiParseLinks,
        private paginationConfig: PaginationConfig,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private datePipe: DatePipe
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 1;
        this.reverse = false;
        this.predicate = 'auditEventDate';
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
    }

    loadPage(page: number) {
        this.page = page;
        this.transition();
    }

    ngOnInit() {
        this.getToday();
        this.previousMonth();
        this.onChangeDate();
    }

    onChangeDate() {
        this.auditsService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort(),
            fromDate: this.dateToString(this.fromDate),
            toDate: this.dateToString(this.toDate)
        }).subscribe((res) => {

            this.audits = res.json();
            this.links = this.parseLinks.parse(res.headers.get('link'));
            this.totalItems = + res.headers.get('X-Total-Count');
        });
    }

    previousMonth() {
        const dateFormat = 'yyyy-MM-dd';
        let fromDate: Date = new Date();

        if (fromDate.getMonth() === 0) {
            fromDate = new Date(fromDate.getFullYear() - 1, 11, fromDate.getDate());
        } else {
            fromDate = new Date(fromDate.getFullYear(), fromDate.getMonth() - 1, fromDate.getDate());
        }

        this.fromDate = fromDate;
    }

    getToday() {
        const dateFormat = 'yyyy-MM-dd';
        // Today + 1 day - needed if the current day must be included
        const today: Date = new Date();
        today.setDate(today.getDate() + 1);
        const date = new Date(today.getFullYear(), today.getMonth(), today.getDate());
        this.toDate = date;
        this.today = date;
    }

    transition() {
        this.router.navigate(['/audits'], {
            queryParams: {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    loadAll() {
        this.auditsService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort(),
            fromDate: this.dateToString(this.fromDate), toDate: this.dateToString(this.toDate)
        }).subscribe((res) => {
            this.audits = res.json();
            this.links = this.parseLinks.parse(res.headers.get('link'));
            this.totalItems = + res.headers.get('X-Total-Count');
        });
    }

    sort() {
        const sort = this.predicate + ',' + (this.reverse ? 'asc' : 'desc');
        return sort;
    }

    private dateToString(date: Date): string {
        const dateFormat = 'yyyy-MM-dd';
        return this.datePipe.transform(date, dateFormat)
    }
}
