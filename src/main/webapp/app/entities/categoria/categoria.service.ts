import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Categoria } from './categoria.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CategoriaService {

    private resourceUrl = 'api/categorias';

    constructor(private http: Http) { }

    create(categoria: Categoria): Observable<Categoria> {
        const copy = this.convert(categoria);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(categoria: Categoria): Observable<Categoria> {
        const copy = this.convert(categoria);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Categoria> {
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

    private convert(categoria: Categoria): Categoria {
        const copy: Categoria = Object.assign({}, categoria);
        return copy;
    }
}
