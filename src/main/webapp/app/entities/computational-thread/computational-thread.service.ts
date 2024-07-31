import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { ComputationalThreads, ComputationalThreadsBase } from './computational-thread.model';
import { Observable } from 'rxjs';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { ComputationalThreadExecution, Result } from './computational-thread-execution.model';

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

    public delete(idThread: number): Observable<ComputationalThreads> {
        return this.http
            .delete(`${this.resourceUrl}/${idThread}`)
            .map((response) => this.convertItemToComputationalThread(response.json()));
    }

    public restore(idThread: number): Observable<ComputationalThreads> {
        return this.http
            .put(`${this.resourceUrl}/${idThread}/restore`, null)
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

    private convertItemToBaseComputationalThread(entity: any): ComputationalThreadsBase {
        return Object.assign(new ComputationalThreadsBase(), entity);
    }

    // EXECUTIONS
    public createExecution(
        idThread: Number,
        computationalThreadExecution: ComputationalThreadExecution
    ): Observable<string> {
        return this.http
            .post(`${this.resourceUrl}/${idThread}/create-execution`, computationalThreadExecution)
            .map((response) => response.text());
    }

    public findAllExecutions(idThread: number, req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http
            .get(`${this.resourceUrl}/${idThread}/executions`, options)
            .map((response) => this.convertResponseToExecutionResponseWrapper(response));
    }

    public existExecutionByResult(idThread: Number, stateExecution: Result[]): Observable<boolean> {
        const options = createRequestOption({ stateExecution: stateExecution });
        return this.http
            .get(`${this.resourceUrl}/${idThread}/existExecution`, options)
            .map((response) => response.json());
    }

    private convertResponseToExecutionResponseWrapper(response: Response): ResponseWrapper {
        const jsonResponse = response
            .json()
            .map((element: any) => this.convertItemToComputationalThreadExecution(element));
        return new ResponseWrapper(response.headers, jsonResponse, response.status);
    }

    private convertItemToComputationalThreadExecution(entity: any): ComputationalThreadExecution {
        return Object.assign(new ComputationalThreadExecution(), entity);
    }
}
