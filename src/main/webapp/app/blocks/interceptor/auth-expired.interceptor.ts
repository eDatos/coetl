import { Observable } from 'rxjs';
import { Injector } from '@angular/core';
import { LoginService } from '../../shared';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { catchError } from 'rxjs/operators';

export class AuthExpiredInterceptor implements HttpInterceptor {
    constructor(private injector: Injector) {}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(
            catchError((error) => {
                if (error.status === 401) {
                    const loginService: LoginService = this.injector.get(LoginService);
                    loginService.logout();
                    loginService.login();
                }
                return Observable.throw(error);
            })
        );
    }
}
