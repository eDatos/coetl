import { Injectable } from '@angular/core';
import { LocalStorageService, SessionStorageService } from 'ng2-webstorage';

import { InstallationService } from '../../shared';

@Injectable()
export class FileService {
    public resourceUrl = 'api/files';

    constructor(
        private localStorage: LocalStorageService,
        private sessionStorage: SessionStorageService,
        private installationService: InstallationService
    ) {}

    download(id: number) {
        window.open(`${this.resourceUrl}/${id}/download?bearerToken=${this.getAuthToken()}`);
    }

    private getAuthToken() {
        return (
            this.localStorage.retrieve(
                this.installationService.getAuthenticationTokenByInstace()
            ) ||
            this.sessionStorage.retrieve(this.installationService.getAuthenticationTokenByInstace())
        );
    }
}
