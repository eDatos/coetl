/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ArteApplicationTemplateTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CategoriaDetailComponent } from '../../../../../../main/webapp/app/entities/categoria/categoria-detail.component';
import { CategoriaService } from '../../../../../../main/webapp/app/entities/categoria/categoria.service';
import { Categoria } from '../../../../../../main/webapp/app/entities/categoria/categoria.model';

describe('Component Tests', () => {

    describe('Categoria Management Detail Component', () => {
        let comp: CategoriaDetailComponent;
        let fixture: ComponentFixture<CategoriaDetailComponent>;
        let service: CategoriaService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ArteApplicationTemplateTestModule],
                declarations: [CategoriaDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CategoriaService,
                    JhiEventManager
                ]
            }).overrideTemplate(CategoriaDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CategoriaDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CategoriaService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Categoria(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.categoria).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
