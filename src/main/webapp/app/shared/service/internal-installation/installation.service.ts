import { Injectable } from '@angular/core';
import { TOKEN_AUTH_NAME } from '../../../app.constants';
import { ConfigService } from '../../../config/config.service';

@Injectable()
export class InstallationService {
    private _type: string;
    private _instance: string;
    constructor(private configService: ConfigService) {
        this._type = this.configService.getConfig().installation.type;
        this._instance = this.configService.getConfig().installation.instance;
    }

    isInternalType(): boolean {
        return this._type.toUpperCase() === 'INTERNAL';
    }

    isCoetlLabInstance(): boolean {
        return this._instance.toUpperCase() === 'COETLLAB';
    }

    getAuthenticationTokenByInstace(): string {
        return TOKEN_AUTH_NAME + '_' + this._instance.toLowerCase();
    }
}
