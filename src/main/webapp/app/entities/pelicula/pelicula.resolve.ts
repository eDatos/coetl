import { Injectable } from '@angular/core';
import { Resolve } from '@angular/router';
import { ActivatedRouteSnapshot } from '@angular/router';
import { Observable } from 'rxjs';

import { Pelicula } from './pelicula.model';
import { PeliculaService } from './pelicula.service';

@Injectable()
export class PeliculaResolve implements Resolve<Pelicula> {
    constructor(private peliculaService: PeliculaService) {
    }
    resolve(route: ActivatedRouteSnapshot): Observable<Pelicula> {
        const paramsId = route.params['id'];
        return this.peliculaService.find(paramsId);
    }
}
