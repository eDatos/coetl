import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable, ReplaySubject } from 'rxjs';

import { createRequestOption, ResponseWrapper } from '../../shared';
import { Actor } from './actor.model';

@Injectable()
export class ActorService {

    private resourceUrl = 'api/actores';

    actorSource: ReplaySubject<Actor> = new ReplaySubject<Actor>();

    constructor(private http: Http) { }

    create(actor: Actor): Observable<Actor> {
        const copy = this.convert(actor);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(actor: Actor): Observable<Actor> {
        const copy = this.convert(actor);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Actor> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return this.convert(res.json());
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

    asociarDocumento(actorId: number, documentoId: number): Observable<Actor> {
        return this.http.put(`${this.resourceUrl}/${actorId}/documento/${documentoId}`, null).map((res: Response) => {
            return this.convert(res.json())
        });
    }

    desasociarDocumento(actorId: number, documentoId: number): Observable<Actor> {
        return this.http.delete(`${this.resourceUrl}/${actorId}/documento/${documentoId}`).map((res: Response) => {
            return this.convert(res.json());
        });
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(actor: Actor): Actor {
        const copy: Actor = Object.assign(new Actor(), actor);
        return copy;
    }
}
