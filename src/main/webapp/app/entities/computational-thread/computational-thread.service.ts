import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

@Injectable()
export class ComputationalThreadService {
    private resourceUrl = 'api/computational-threads';

    constructor(private http: Http) {}
}
