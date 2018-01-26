/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ArteApplicationTemplateTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PeliculaDetailComponent } from '../../../../../../main/webapp/app/entities/pelicula/pelicula-detail.component';
import { PeliculaService } from '../../../../../../main/webapp/app/entities/pelicula/pelicula.service';
import { Pelicula } from '../../../../../../main/webapp/app/entities/pelicula/pelicula.model';

describe('Component Tests', () => {

    describe('Pelicula Management Detail Component', () => {
        let comp: PeliculaDetailComponent;
        let fixture: ComponentFixture<PeliculaDetailComponent>;
        let service: PeliculaService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ArteApplicationTemplateTestModule],
                declarations: [PeliculaDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PeliculaService,
                    JhiEventManager
                ]
            }).overrideTemplate(PeliculaDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PeliculaDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PeliculaService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Pelicula(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.pelicula).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
