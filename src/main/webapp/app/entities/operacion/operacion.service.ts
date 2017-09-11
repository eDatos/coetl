import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Operacion } from './operacion.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class OperacionService {

    private resourceUrl = 'api/operaciones';

    constructor(private http: Http) { }

    find(id: number): Observable<Operacion> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(operacion: Operacion): Operacion {
        const copy: Operacion = Object.assign({}, operacion);
        return copy;
    }
}
