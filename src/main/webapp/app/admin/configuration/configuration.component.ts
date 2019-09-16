import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';

import { JhiConfigurationService } from './configuration.service';
import { HasTitlesContainer } from '../../shared';

@Component({
    selector: 'jhi-configuration',
    templateUrl: './configuration.component.html',
    styleUrls: ['./configuration.component.scss']
})
export class JhiConfigurationComponent implements OnInit, HasTitlesContainer {
    allConfiguration: any = [];
    configuration: any = [];
    configKeys: any[];
    filter: string;
    orderProp: string;
    reverse: boolean;

    @ViewChild('titlesContainer') titlesContainer: ElementRef;
    public instance: JhiConfigurationComponent;

    constructor(private configurationService: JhiConfigurationService) {
        this.configKeys = [];
        this.filter = '';
        this.orderProp = 'prefix';
        this.reverse = false;
        this.instance = this;
    }

    keys(dict): Array<string> {
        return dict === undefined ? [] : Object.keys(dict);
    }

    ngOnInit() {
        this.configurationService.get().subscribe((configuration) => {
            this.configuration = configuration;

            for (const config of configuration) {
                if (config.properties !== undefined) {
                    this.configKeys.push(Object.keys(config.properties));
                }
            }
        });

        this.configurationService.getEnv().subscribe((configuration) => {
            this.allConfiguration = configuration;
        });
    }

    getTitlesContainer() {
        return this.titlesContainer;
    }
}
