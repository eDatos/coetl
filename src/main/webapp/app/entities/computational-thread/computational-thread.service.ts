import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { ComputationalThreads, ComputationalThreadsBase } from './computational-thread.model';
import { Observable } from 'rxjs';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ComputationalThreadService {
    private resourceUrl = 'api/computational-threads';

    constructor(private http: Http) {}

    public create(computationalThread: ComputationalThreads): Observable<ComputationalThreads> {
        return this.http
            .post(this.resourceUrl, computationalThread)
            .map((response) => this.convertItemToComputationalThread(response.json()));
    }

    public update(computationalThread: ComputationalThreads): Observable<ComputationalThreads> {
        return this.http
            .put(this.resourceUrl, computationalThread)
            .map((response) => this.convertItemToComputationalThread(response.json()));
    }

    public find(idThread: number): Observable<ComputationalThreads> {
        return this.http
            .get(`${this.resourceUrl}/${idThread}`)
            .map((response) => this.convertItemToComputationalThread(response.json()));
    }

    private convertItemToComputationalThread(entity: any): ComputationalThreads {
        return Object.assign(new ComputationalThreads(), entity);
    }

    public query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        options.params.set('lastExecution', req.lastExecution);
        options.params.set('lastExecutionByResult', req.lastExecutionByResult);
        return this.http
            .get(this.resourceUrl, options)
            .map((response) =>
                this.convertResponseToComputationalThreadBaseResponseWrapper(response)
            );
    }

    private convertResponseToComputationalThreadBaseResponseWrapper(
        response: Response
    ): ResponseWrapper {
        const jsonResponse = response
            .json()
            .map((element: any) => this.convertItemToBaseComputationalThread(element));
        return new ResponseWrapper(response.headers, jsonResponse, response.status);
    }

    private convertItemToBaseComputationalThread(entity: any): ComputationalThreads {
        return Object.assign(new ComputationalThreadsBase(), entity);
    }
}
