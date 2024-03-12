import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { ComputationalThreads } from './computational-thread.model';
import { Observable } from 'rxjs';

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
}
