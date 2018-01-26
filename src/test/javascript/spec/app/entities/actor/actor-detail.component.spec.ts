/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ArteApplicationTemplateTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ActorDetailComponent } from '../../../../../../main/webapp/app/entities/actor/actor-detail.component';
import { ActorService } from '../../../../../../main/webapp/app/entities/actor/actor.service';
import { Actor } from '../../../../../../main/webapp/app/entities/actor/actor.model';

describe('Component Tests', () => {

    describe('Actor Management Detail Component', () => {
        let comp: ActorDetailComponent;
        let fixture: ComponentFixture<ActorDetailComponent>;
        let service: ActorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ArteApplicationTemplateTestModule],
                declarations: [ActorDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ActorService,
                    JhiEventManager
                ]
            }).overrideTemplate(ActorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ActorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ActorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Actor(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.actor).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
