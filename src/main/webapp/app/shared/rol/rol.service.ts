import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Rol } from './rol.model';
import { ResponseWrapper } from '../model/response-wrapper.model';
import { createRequestOption } from '../model/request-util';

@Injectable()
export class RolService {

    private resourceUrl = 'api/roles';

    constructor(private http: Http) { }

    // TODO SECRETARIA-51 ELIMINAR para dejar solo query
    roles(): Observable<string[]> {
        return this.http.get('api/roles').map((res: Response) => {
            const json = res.json();
            return <string[]>json;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    find(name: string): Observable<Rol> {
        return this.http.get(`${this.resourceUrl}/${name}`).map((res: Response) => {
            return res.json();
        });
    }

    create(rol: Rol): Observable<ResponseWrapper> {
        return this.http.post(this.resourceUrl, rol)
            .map((res: Response) => this.convertResponse(res));
    }

    update(rol: Rol): Observable<ResponseWrapper> {
        return this.http.put(this.resourceUrl, rol)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(rol: Rol): Observable<ResponseWrapper> {
        return this.http.delete(`${this.resourceUrl}/${rol}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

}
