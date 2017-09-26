import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { NgbPaginationConfig } from '@ng-bootstrap/ng-bootstrap';
import { JhiParseLinks } from 'ng-jhipster';
import { SecretariaLibroTestModule } from '../../../test.module';
import { PaginationConfig } from '../../../../../../main/webapp/app/blocks/config/uib-pagination.config'
import { AuditsComponent } from '../../../../../../main/webapp/app/admin/audits/audits.component';
import { AuditsService } from '../../../../../../main/webapp/app/admin/audits/audits.service';
import { ITEMS_PER_PAGE } from '../../../../../../main/webapp/app/shared';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Observable';

function getDate(isToday = true) {
    let date: Date = new Date();
    if (isToday) {
        // Today + 1 day - needed if the current day must be included
        date.setDate(date.getDate() + 1);
    } else {
        // get last month
        if (date.getMonth() === 0) {
            date = new Date(date.getFullYear() - 1, 11, date.getDate());
        } else {
            date = new Date(date.getFullYear(), date.getMonth() - 1, date.getDate());
        }
    }
    return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;
}

describe('Component Tests', () => {

    describe('AuditsComponent', () => {

        let comp: AuditsComponent;
        let fixture: ComponentFixture<AuditsComponent>;
        let service: AuditsService;
        let routeStub: any;
        const data = {
            pagingParams: {
                page: 1,
                ascending: true,
                predicate: 'auditEventDate'
            }
        };

        beforeEach(async(() => {
            routeStub = {
                data: Observable.of(data)
            }
            TestBed.configureTestingModule({
                imports: [SecretariaLibroTestModule],
                declarations: [AuditsComponent],
                providers: [
                    AuditsService,
                    NgbPaginationConfig,
                    JhiParseLinks,
                    PaginationConfig,
                    DatePipe,
                    { provide: Router, useValue: null },
                    { provide: ActivatedRoute, useValue: routeStub },
                ]
            }).overrideTemplate(AuditsComponent, '')
                .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AuditsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AuditsService);
        });

        describe('today function ', () => {
            it('should set toDate to current date', () => {
                comp.getToday();
                expect(comp.toDate.getDate()).toBe(getDate().getDate());
                expect(comp.toDate.getMonth()).toBe(getDate().getMonth());
                expect(comp.toDate.getFullYear()).toBe(getDate().getFullYear());
            });
        });

        describe('previousMonth function ', () => {
            it('should set fromDate to current date', () => {
                comp.previousMonth();
                expect(comp.fromDate.getDate()).toBe(getDate(false).getDate());
                expect(comp.fromDate.getMonth()).toBe(getDate(false).getMonth());
                expect(comp.fromDate.getFullYear()).toBe(getDate(false).getFullYear());
            });
        });

        describe('By default, on init', () => {
            it('should set all default values correctly', () => {
                fixture.detectChanges();
                expect(comp.toDate.getDate()).toBe(getDate().getDate());
                expect(comp.toDate.getMonth()).toBe(getDate().getMonth());
                expect(comp.toDate.getFullYear()).toBe(getDate().getFullYear());
                expect(comp.fromDate.getDate()).toBe(getDate(false).getDate());
                expect(comp.fromDate.getMonth()).toBe(getDate(false).getMonth());
                expect(comp.fromDate.getFullYear()).toBe(getDate(false).getFullYear());
                expect(comp.itemsPerPage).toBe(ITEMS_PER_PAGE);
                expect(comp.page).toBe(1);
                expect(comp.reverse).toBeTruthy();
            });
        });
    });
});
