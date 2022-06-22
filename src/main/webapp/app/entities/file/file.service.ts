import { Injectable, Injector } from '@angular/core';
import { LocalStorageService, SessionStorageService } from 'ng2-webstorage';

import { InstallationService } from '../../shared';

@Injectable()
export class FileService {
    public resourceUrl = 'api/files';

    private authenticationToken: string;
    private installationService;

    constructor(
        private localStorage: LocalStorageService,
        private sessionStorage: SessionStorageService,
        private injector: Injector
    ) {
        this.installationService = this.injector.get(InstallationService);
        this.authenticationToken = this.installationService.getAuthenticationTokenByInstance();
    }

    download(id: number) {
        window.open(`${this.resourceUrl}/${id}/download?bearerToken=${this.getAuthToken()}`);
    }

    private getAuthToken() {
        return (
            this.localStorage.retrieve(this.authenticationToken) ||
            this.sessionStorage.retrieve(this.authenticationToken)
        );
    }
}
