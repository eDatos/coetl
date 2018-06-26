import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Rol } from './rol.model';
import { ResponseWrapper } from '../model/response-wrapper.model';
import { createRequestOption } from '../model/request-util';
import { orderParamsToQuery } from '../index';

@Injectable()
export class RolService {

    constructor() { }

    public rolFromString(roles: any[]): Rol[] {
        if (typeof roles === 'string') {
            roles = [roles];
        }
        if (roles && roles.length > 0) {
            roles = roles.map((stringRol) => Object.keys(Rol).find((objectRol) => objectRol.toUpperCase() === stringRol.toUpperCase()))
                .filter((rol) => !!rol);
        }
        return roles;
    }
}
