/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SecretariaLibroTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { OperacionDetailComponent } from '../../../../../../main/webapp/app/entities/operacion/operacion-detail.component';
import { OperacionService } from '../../../../../../main/webapp/app/entities/operacion/operacion.service';
import { Operacion } from '../../../../../../main/webapp/app/entities/operacion/operacion.model';

describe('Component Tests', () => {

    describe('Operacion Management Detail Component', () => {
        let comp: OperacionDetailComponent;
        let fixture: ComponentFixture<OperacionDetailComponent>;
        let service: OperacionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SecretariaLibroTestModule],
                declarations: [OperacionDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    OperacionService,
                    JhiEventManager
                ]
            }).overrideTemplate(OperacionDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OperacionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OperacionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Operacion(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.operacion).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
