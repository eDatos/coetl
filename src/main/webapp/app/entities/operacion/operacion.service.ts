import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Operacion } from './operacion.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class OperacionService {

    private resourceUrl = 'api/operacions';

    constructor(private http: Http) { }

    create(operacion: Operacion): Observable<Operacion> {
        const copy = this.convert(operacion);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(operacion: Operacion): Observable<Operacion> {
        const copy = this.convert(operacion);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

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

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
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
