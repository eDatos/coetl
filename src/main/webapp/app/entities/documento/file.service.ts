import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { LocalStorageService, SessionStorageService } from 'ng2-webstorage';
import { Observable } from 'rxjs';

import { TOKEN_AUTH_NAME } from '../../app.constants';
import { ResponseWrapper } from '../../shared';
import { File } from './file.model';

@Injectable()
export class FileService {
    public resourceUrl = 'api/files';

    constructor(
        private http: Http,
        private localStorage: LocalStorageService,
        private sessionStorage: SessionStorageService
    ) {}

    download(id: number) {
        window.open(`${this.resourceUrl}/${id}/download?bearerToken=${this.getAuthToken()}`);
    }

    private getAuthToken() {
        return (
            this.localStorage.retrieve(TOKEN_AUTH_NAME) ||
            this.sessionStorage.retrieve(TOKEN_AUTH_NAME)
        );
    }

    delete(id: number) {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    query(req?: any): Observable<ResponseWrapper> {
        throw new Error('TODO - Not implemented');
    }

    find(id: number): Observable<File> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => res.json());
    }
}
