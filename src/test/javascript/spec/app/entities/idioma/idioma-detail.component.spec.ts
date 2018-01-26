/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ArteApplicationTemplateTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { IdiomaDetailComponent } from '../../../../../../main/webapp/app/entities/idioma/idioma-detail.component';
import { IdiomaService } from '../../../../../../main/webapp/app/entities/idioma/idioma.service';
import { Idioma } from '../../../../../../main/webapp/app/entities/idioma/idioma.model';

describe('Component Tests', () => {

    describe('Idioma Management Detail Component', () => {
        let comp: IdiomaDetailComponent;
        let fixture: ComponentFixture<IdiomaDetailComponent>;
        let service: IdiomaService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ArteApplicationTemplateTestModule],
                declarations: [IdiomaDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    IdiomaService,
                    JhiEventManager
                ]
            }).overrideTemplate(IdiomaDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(IdiomaDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IdiomaService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Idioma(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.idioma).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
