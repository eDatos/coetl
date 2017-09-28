import { SpyObject } from './spyobject';
import Spy = jasmine.Spy;
import { UserService } from '../../../../main/webapp/app/shared/index';

export class MockUserService extends SpyObject {

    getSpy: Spy;
    saveSpy: Spy;
    fakeResponse: any;

    constructor() {
        super(UserService);

        this.fakeResponse = null;
        this.getSpy = this.spy('getLogueado').andReturn(this);
        this.saveSpy = this.spy('update').andReturn(this);
    }

    subscribe(callback: any) {
        callback(this.fakeResponse);
    }

    setResponse(json: any): void {
        this.fakeResponse = json;
    }
}
