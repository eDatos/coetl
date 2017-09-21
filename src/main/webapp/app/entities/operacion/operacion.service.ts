import { Injectable } from '@angular/core';
import { Http, Response, BaseRequestOptions, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Operacion } from './operacion.model';
import { ResponseWrapper, createRequestOption, orderParamsToQuery } from '../../shared';

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
        const options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    queryAll(): Observable<ResponseWrapper> {
        return this.http.get(this.resourceUrl + '/all')
            .map((res: Response) => this.convertResponse(res));
    }

    operacionFromString(operaciones: any[]): Operacion[] {
        if (typeof operaciones === 'string') {
            operaciones = [operaciones];
        }
        if (operaciones && operaciones.length > 0) {
            operaciones = operaciones.map((operacion) => {
                if (typeof operacion === 'string') {
                    const operacionValues = operacion.split(':');
                    if (operacionValues.length >= 2) {
                        return new Operacion(null, operacionValues[0], operacionValues[1]);
                    }
                }
                return operacion;
            });
        }
        return operaciones;
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(operacion: Operacion): Operacion {
        const copy: Operacion = Object.assign({}, operacion);
        return copy;
    }

    private createRequestOption = (req?: any): BaseRequestOptions => {
        const options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            const params: URLSearchParams = new URLSearchParams();
            let query = req.query || '';
            if (req.sort) {
                query += ' ORDER BY ' + orderParamsToQuery(req.sort);
            }
            params.set('query', query);
            options.params = params;
        }
        return options;
    };

}
