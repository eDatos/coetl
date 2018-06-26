import { Principal } from './../../shared/auth/principal.service';
import { Component, OnInit } from '@angular/core';

import { Log } from './log.model';
import { LogsService } from './logs.service';
import { Rol } from '../../shared';

@Component({
    selector: 'jhi-logs',
    templateUrl: './logs.component.html',
    styles: ['.entity { padding-top: 111px; }']
})
export class LogsComponent implements OnInit {

    loggers: Log[];
    filter: string;
    orderProp: string;
    reverse: boolean;

    constructor(
        private logsService: LogsService,
        private principal: Principal,
    ) {
        this.filter = '';
        this.orderProp = 'name';
        this.reverse = false;
    }

    ngOnInit() {
        this.logsService.findAll().subscribe((loggers) => this.loggers = loggers);
    }

    changeLevel(name: string, level: string) {
        this.principal.hasRoles([Rol.ADMIN]).then((result) => {
            if (result) {
                const log = new Log(name, level);
                this.logsService.changeLevel(log).subscribe(() => {
                    this.logsService.findAll().subscribe((loggers) => this.loggers = loggers);
                });
            }
        });
    }
}
