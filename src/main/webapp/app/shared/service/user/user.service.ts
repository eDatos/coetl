import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { User } from './user.model';
import { ResponseWrapper } from '../../model/response-wrapper.model';
import { createRequestOption } from '../../model/request-util';

@Injectable()
export class UserService {
    private resourceUrl = 'api/usuarios';

    constructor(private http: Http) { }

    create(user: User): Observable<ResponseWrapper> {
        return this.http.post(this.resourceUrl, user)
            .map((res: Response) => this.convertResponse(res));
    }

    update(user: User): Observable<ResponseWrapper> {
        return this.http.put(this.resourceUrl, user)
            .map((res: Response) => this.convertResponse(res));
    }

    find(login: string, includeDeleted = true): Observable<User> {
        const options = createRequestOption({ includeDeleted });
        return this.http.get(`${this.resourceUrl}/${login}`, options).map((res: Response) => res.json());
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(login: string): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${login}`);
    }

    restore(login: string): Observable<ResponseWrapper> {
        return this.http.put(`${this.resourceUrl}/${login}/restore`, null)
            .map((res: Response) => this.convertResponse(res));
    }

    buscarUsuarioEnLdap(login: string): Observable<User> {
        return this.http.get(`${this.resourceUrl}/${login}/ldap`)
            .map((res: Response) => res.json());
    }

    getLogueado(): Observable<any> {
        return this.http.get('api/usuario')
            .map((res: Response) => res.json());
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }
}
