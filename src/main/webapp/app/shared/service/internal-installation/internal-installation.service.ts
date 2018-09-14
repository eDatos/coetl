import { Injectable, OnInit } from '@angular/core';
import { ConfigService } from '../../../config/config.service';

@Injectable()
export class InternalInstallationService {
    private _type: string;
    constructor(private configService: ConfigService) {
        console.log(this.configService.getConfig());
        this._type = this.configService.getConfig().internalInstallation;
    }

    isInternalApp(): boolean {
        return this._type === 'INTERNAL';
    }
}
