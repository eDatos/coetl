import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs';
import { ExternalItem, StadisticalOperation } from '.';
import { createRequestOption, ResponseWrapper } from '../../shared';

@Injectable()
export class ExternalItemService {
    public resourceUrl = 'api/external-item';

    constructor(private http: Http) {}

    public findAll(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http
            .get(`${this.resourceUrl}/statistical-operations`, options)
            .map((response) => this.convertItemToResponseWrapper(response));
    }

    private convertItemToResponseWrapper(response: Response): ResponseWrapper {
        const jsonResponse = response
            .json()
            .map((element: any) => this.convertItemToStadisticalOperation(element));
        return new ResponseWrapper(response.headers, jsonResponse, response.status);
    }

    private convertItemToStadisticalOperation(entity: any): StadisticalOperation {
        return Object.assign(new StadisticalOperation(), entity);
    }
}
