import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { Observable } from 'rxjs';

import { Etl } from './etl.model';
import { EtlService } from './etl.service';

@Injectable()
export class EtlResolve implements Resolve<Etl> {
    constructor(private etlService: EtlService) {}

    resolve(route: ActivatedRouteSnapshot): Observable<Etl> {
        const idEtl = route.params['idEtl'];
        return this.etlService.find(idEtl);
    }
}
