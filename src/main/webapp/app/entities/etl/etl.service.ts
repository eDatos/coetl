import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs';

import { createRequestOption, ResponseWrapper } from '../../shared';
import { Etl } from './etl.model';

@Injectable()
export class EtlService {
    public readonly resourceUrl = '/api/etls';

    constructor(private http: Http) {}

    public create(etl: Etl): Observable<Etl> {
        return this.http
            .post(this.resourceUrl, etl)
            .map((response) => this.convertItemToEtl(response.json()));
    }

    public update(etl: Etl): Observable<Etl> {
        return this.http
            .put(this.resourceUrl, etl)
            .map((response) => this.convertItemToEtl(response.json()));
    }

    public delete(idEtl: number): Observable<Etl> {
        return this.http
            .delete(`${this.resourceUrl}/${idEtl}`)
            .map((response) => this.convertItemToEtl(response.json()));
    }

    public restore(idEtl: number): Observable<Etl> {
        return this.http
            .delete(`${this.resourceUrl}/${idEtl}/restore`)
            .map((response) => this.convertItemToEtl(response.json()));
    }

    public find(idEtl: number): Observable<Etl> {
        return this.http
            .get(`${this.resourceUrl}/${idEtl}`)
            .map((response) => this.convertItemToEtl(response.json()));
    }

    public query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http
            .get(this.resourceUrl, options)
            .map((response) => this.convertResponseToResponseWrapper(response));
    }

    private convertResponseToResponseWrapper(response: Response): ResponseWrapper {
        const jsonResponse = response.json().map((element: any) => this.convertItemToEtl(element));
        return new ResponseWrapper(response.headers, jsonResponse, response.status);
    }

    private convertItemToEtl(entity: any): Etl {
        return Object.assign(new Etl(), entity);
    }
}
