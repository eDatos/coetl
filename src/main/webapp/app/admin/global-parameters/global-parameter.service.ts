import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs';
import { Parameter } from '../../entities/parameter';

import { createRequestOption, ResponseWrapper } from '../../shared';

@Injectable()
export class GloablParameterService {
    public readonly resourceUrl = 'api/global-parameters';

    constructor(private http: Http) {}

    public createParameter(parameter: Parameter): Observable<Parameter> {
        return this.http
            .post(`${this.resourceUrl}`, parameter)
            .map((response: Response) => this.convertItemToParameter(response.json()));
    }

    public updateParameter(parameter: Parameter): Observable<Parameter> {
        return this.http
            .put(`${this.resourceUrl}`, parameter)
            .map((response: Response) => this.convertItemToParameter(response.json()));
    }

    public deleteParameter(idParameter: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${idParameter}`);
    }

    public findAllParameters(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http
            .get(`${this.resourceUrl}`, options)
            .map((response: Response) => this.convertResponseToParameterReponseWrapper(response));
    }

    public find(idParameter: number): Observable<Parameter> {
        return this.http
            .get(`${this.resourceUrl}/${idParameter}`)
            .map((response) => this.convertItemToParameter(response.json()));
    }

    public decodeParameter(idParameter: number): Observable<Parameter> {
        return this.http
            .get(`${this.resourceUrl}/${idParameter}/decode`)
            .map((response: Response) => this.convertItemToParameter(response.json()));
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
