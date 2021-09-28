import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs';

import { createRequestOption, ResponseWrapper } from '../../shared';
import { Etl, EtlBase } from './etl.model';
import { Execution } from '../execution/execution.model';
import { Parameter } from '../parameter';

@Injectable()
export class EtlService {
    public readonly resourceUrl = 'api/etls';

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
            .put(`${this.resourceUrl}/${idEtl}/restore`, null)
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
            .map((response) => this.convertResponseToEtlBaseResponseWrapper(response));
    }

    public execute(idEtl: Number): Observable<string> {
        return this.http
            .get(`${this.resourceUrl}/${idEtl}/execute`)
            .map((response) => response.text());
    }

    public findAllExecutions(idEtl: number, req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http
            .get(`${this.resourceUrl}/${idEtl}/executions`, options)
            .map((response) => this.convertResponseToExecutionResponseWrapper(response));
    }

    public createParameter(idEtl: number, parameter: Parameter): Observable<Parameter> {
        return this.http
            .post(`${this.resourceUrl}/${idEtl}/parameters`, parameter)
            .map((response: Response) => this.convertItemToParameter(response.json()));
    }

    public updateParameter(idEtl: number, parameter: Parameter): Observable<Parameter> {
        return this.http
            .put(`${this.resourceUrl}/${idEtl}/parameters`, parameter)
            .map((response: Response) => this.convertItemToParameter(response.json()));
    }

    public deleteParameter(idEtl: number, idParameter: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${idEtl}/parameters/${idParameter}`);
    }

    public findParameter(idEtl: number, idParameter: number): Observable<Parameter> {
        return this.http
            .get(`${this.resourceUrl}/${idEtl}/parameters/${idParameter}`)
            .map((response: Response) => this.convertItemToParameter(response));
    }

    public findAllParameters(idEtl: number): Observable<ResponseWrapper> {
        return this.http
            .get(`${this.resourceUrl}/${idEtl}/parameters`)
            .map((response: Response) => this.convertResponseToParameterReponseWrapper(response));
    }

    private convertResponseToEtlBaseResponseWrapper(response: Response): ResponseWrapper {
        const jsonResponse = response
            .json()
            .map((element: any) => this.convertItemToBaseEtl(element));
        return new ResponseWrapper(response.headers, jsonResponse, response.status);
    }

    private convertItemToBaseEtl(entity: any): Etl {
        return Object.assign(new EtlBase(), entity);
    }

    private convertItemToEtl(entity: any): Etl {
        return Object.assign(new Etl(), entity);
    }

    private convertResponseToExecutionResponseWrapper(response: Response): ResponseWrapper {
        const jsonResponse = response
            .json()
            .map((element: any) => this.convertItemToExecution(element));
        return new ResponseWrapper(response.headers, jsonResponse, response.status);
    }

    private convertItemToExecution(entity: any): Etl {
        return Object.assign(new Execution(), entity);
    }

    private convertResponseToParameterReponseWrapper(response: Response): ResponseWrapper {
        const jsonResponse = response
            .json()
            .map((element: any) => this.convertItemToParameter(element));
        return new ResponseWrapper(response.headers, jsonResponse, response.status);
    }

    private convertItemToParameter(entity: any): Parameter {
        return Object.assign(new Parameter(), entity);
    }
}
