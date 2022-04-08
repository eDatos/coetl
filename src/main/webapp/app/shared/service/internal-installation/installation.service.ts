import { Injectable } from '@angular/core';
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
}
