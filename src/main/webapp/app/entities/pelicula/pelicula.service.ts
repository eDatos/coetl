import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { JhiDateUtils } from 'ng-jhipster';
import { Observable } from 'rxjs/Rx';

import { createRequestOption, ResponseWrapper } from '../../shared';
import { Pelicula } from './pelicula.model';

@Injectable()
export class PeliculaService {

    private resourceUrl = 'api/peliculas';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(pelicula: Pelicula): Observable<Pelicula> {
        return this.http.post(this.resourceUrl, pelicula).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(pelicula: Pelicula): Observable<Pelicula> {
        return this.http.put(this.resourceUrl, pelicula).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Pelicula> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
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

    deleteSelection(req: any): Observable<Response> {
        const options = createRequestOption(req);
        return this.http.delete(this.resourceUrl, options);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.fechaEstreno = this.dateUtils
            .convertDateTimeFromServer(entity.fechaEstreno);
    }
}
