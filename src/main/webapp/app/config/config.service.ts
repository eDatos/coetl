import { Injectable, isDevMode } from '@angular/core';
import { GenericConfig } from './generic-config.interface';

@Injectable()
export class ConfigService {
    getConfig(): GenericConfig {
        return (<any>window).CONFIGURATION;
    }
}
