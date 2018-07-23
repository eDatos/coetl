// FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)

import { ConfigService } from './config.service';
import { NgModule } from '@angular/core';

@NgModule({
    providers: [
        ConfigService,
    ],
})
export class CoetlConfigModule { }
