import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { ComputationalThreads } from './computational-thread.model';
import { ComputationalThreadService } from './computational-thread.service';

@Injectable()
export class ComputationalThreadResolve implements Resolve<ComputationalThreads> {
    constructor(private computationalThreadService: ComputationalThreadService) {}

    resolve(route: ActivatedRouteSnapshot): Observable<ComputationalThreads> {
        const idThread = route.params['idThread'];
        return this.computationalThreadService.find(idThread);
    }
}
