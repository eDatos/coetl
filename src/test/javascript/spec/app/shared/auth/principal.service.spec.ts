// FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)

import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { ArteApplicationTemplateTestModule } from '../../../test.module';

import { MockUserService } from '../../../helpers/mock-user.service';
import { Principal, UserService } from '../../../../../../main/webapp/app/shared/index';
import { SettingsComponent } from '../../../../../../main/webapp/app/account/index';
import { getTestBed } from '@angular/core/testing';
import { OperacionService } from '../../../../../../main/webapp/app/entities/operacion/operacion.service';
import { Observable } from 'rxjs/Observable';
import { Operacion } from '../../../../../../main/webapp/app/entities/operacion/operacion.model';

const accountValue = {
    firstName: 'John',
    email: 'john.doe@mail.com',
    login: 'john',
};

describe('Principal Service', () => {
    let principal: Principal;
    let mockAuth: any;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [ArteApplicationTemplateTestModule],
            providers: [
                Principal,
                {
                    provide: UserService,
                    useClass: MockUserService
                },
                {
                    provide: OperacionService,
                    useClass: OperacionService
                },
            ]
        })
        const testbed = getTestBed();
        principal = testbed.get(Principal);
        mockAuth = testbed.get(UserService);
    }));

    it('should return false if user has no operations', () => {
        // GIVEN
        mockAuth.getLogueado.and.returnValue(Observable.from([accountValue]));

        // WHEN
        let result;
        principal.identity().then(() => result = principal.canDoAnyOperacion(null));

        // THEN
        expect(result).toBeFalsy();
    })

    it('should return false if route and user operations dont match', () => {
        // GIVEN
        const operacion = new Operacion(null, 'LEER', 'ROL');
        accountValue['roles'] = [{ operaciones: [operacion] }];
        mockAuth.getLogueado.and.returnValue(Observable.from([accountValue]));

        // WHEN
        let result;
        principal.identity().then(() => result = principal.canDoAnyOperacion(['WRITE:ROL']));

        // THEN
        expect(result).toBeFalsy();
    })

    it('should return true if route and user operations match', () => {
        // GIVEN
        const operacion = new Operacion(null, 'LEER', 'ROL');
        accountValue['roles'] = [{ operaciones: [operacion] }];
        mockAuth.getLogueado.and.returnValue(Observable.from([accountValue]));

        // WHEN
        principal.identity().then(() =>
            principal.canDoAnyOperacion(['LEER:ROL']))
            .then((result) => {
                // THEN
                expect(result).toBeTruthy();
            })
    })

    it('should return true if any route and user operations match', () => {
        // GIVEN
        const operacion = new Operacion(null, 'LEER', 'ROL');
        accountValue['roles'] = [{ operaciones: [operacion] }];
        mockAuth.getLogueado.and.returnValue(Observable.from([accountValue]));

        // WHEN
        principal.identity().then(() =>
            principal.canDoAnyOperacion(['LEER:ROL', 'LEER:USUARIO']))
            .then((result) => {
                // THEN
                expect(result).toBeTruthy();
            })
    })

    it('should return true if route has no operations needed', () => {
        // GIVEN
        mockAuth.getLogueado.and.returnValue(Observable.from([accountValue]));

        // WHEN
        principal.identity().then(() =>
            principal.canDoAnyOperacion(null))
            .then((result) => {
                // THEN
                expect(result).toBeTruthy();
            })
    })

    it('should return false if route has a non valid operation', () => {
        // GIVEN
        mockAuth.getLogueado.and.returnValue(Observable.from([accountValue]));

        // WHEN
        principal.identity().then(() =>
            principal.canDoAnyOperacion(['NON_VALID_OPERATION']))
            .then((result) => {
                // THEN
                expect(result).toBeFalsy();
            })
    })

});
