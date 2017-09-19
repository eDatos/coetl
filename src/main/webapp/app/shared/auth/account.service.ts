import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { Account } from '../../shared/user/account.model';
import { Rol } from '../index';

@Injectable()
export class AccountService {
    constructor(private http: Http) { }

    get(): Observable<any> {
        return this.http.get('api/account')
            .map((res: Response) => res.json())
            .map((account: Account) => {
                account.roles = account.roles.map((rol) => new Rol(rol.id, rol.nombre, rol.operaciones))
                return account;
            });
    }

    save(account: any): Observable<Response> {
        return this.http.post('api/account', account);
    }
}
