import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { JhiParseLinks } from 'ng-jhipster';
import { ActivatedRoute, Router } from '@angular/router';
import { Audit } from './audit.model';
import { AuditsService } from './audits.service';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-audit',
    templateUrl: './audits.component.html',
    styleUrls: ['audits.component.scss']
})
export class AuditsComponent implements OnInit {

    // Atributos para la paginación
    page: number;
    totalItems: number;
    itemsPerPage: number;

    audits: Audit[];
    fromDate: Date;
    routeData: any;
    links: any;
    orderProp: string;
    reverse: boolean;
    predicate: any;
    toDate: Date;
    today: Date;

    constructor(
        private auditsService: AuditsService,
        private parseLinks: JhiParseLinks,
        private paginationConfig: PaginationConfig,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private datePipe: DatePipe
    ) {
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
            this.itemsPerPage = data['pagingParams'].itemsPerPage;
        });
    }

    ngOnInit() {
        this.getToday();
        this.previousMonth();
        this.activatedRoute.queryParams.subscribe(() => this.loadAll());
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

    loadAll() {
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

    transition() {
        this.router.navigate(['/audits'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
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
