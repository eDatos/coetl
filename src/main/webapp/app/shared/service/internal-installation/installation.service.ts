import { Injectable } from '@angular/core';
import { ConfigService } from '../../../config/config.service';

@Injectable()
export class InstallationService {
    private _type: string;
    constructor(private configService: ConfigService) {
        this._type = this.configService.getConfig().installation.type;
    }

    isInternalType(): boolean {
        return this._type === 'INTERNAL';
    }
}
