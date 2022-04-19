import { Injectable } from '@angular/core';
import { LocalStorageService, SessionStorageService } from 'ng2-webstorage';

import { JHI_TOKEN_AUTH_NAME } from '../../app.constants';

@Injectable()
export class FileService {
    public resourceUrl = 'api/files';

    constructor(
        private localStorage: LocalStorageService,
        private sessionStorage: SessionStorageService
    ) {}

    download(id: number) {
        window.open(`${this.resourceUrl}/${id}/download?bearerToken=${this.getAuthToken()}`);
    }

    private getAuthToken() {
        return (
            this.localStorage.retrieve(JHI_TOKEN_AUTH_NAME) ||
            this.sessionStorage.retrieve(JHI_TOKEN_AUTH_NAME)
        );
    }
}
