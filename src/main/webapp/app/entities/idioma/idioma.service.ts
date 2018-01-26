import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Idioma } from './idioma.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class IdiomaService {

    private resourceUrl = 'api/idiomas';

    constructor(private http: Http) { }

    create(idioma: Idioma): Observable<Idioma> {
        const copy = this.convert(idioma);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(idioma: Idioma): Observable<Idioma> {
        const copy = this.convert(idioma);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Idioma> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
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

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(idioma: Idioma): Idioma {
        const copy: Idioma = Object.assign({}, idioma);
        return copy;
    }
}
