import {
    HttpErrorResponse,
    HttpEvent,
    HttpHandler,
    HttpInterceptor,
    HttpRequest
} from '@angular/common/http';
import { JhiEventManager } from 'ng-jhipster';
import { Observable } from 'rxjs/Observable';
import { catchError } from 'rxjs/operators';

export class ErrorHandlerInterceptor implements HttpInterceptor {
    constructor(private eventManager: JhiEventManager) {}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next
            .handle(req)
            .pipe(
                catchError((err) => {
                    if (err instanceof HttpErrorResponse && err.error instanceof Blob) {
                        const reader: FileReader = new FileReader();

                        const obs = new Observable<HttpEvent<any>>((observer: any) => {
                            reader.onloadend = (e) => {
                                const errorMessage = JSON.parse(reader.result as string);
                                const errUrl =
                                    err.url !== null && err.url !== void 0 ? err.url : undefined;
                                const errorResponse: HttpErrorResponse = new HttpErrorResponse({
                                    error: errorMessage,
                                    headers: err.headers,
                                    status: err.status,
                                    statusText: err.statusText,
                                    url: errUrl
                                });
                                observer.error(errorResponse);
                                observer.complete();
                            };
                        });
                        reader.readAsText(err.error);
                        return obs;
                    }
                    return Observable.throw(err.error);
                })
            )
            .pipe(
                catchError((err) => {
                    if (
                        err.status !==
                        401 /*|| !(err.text() === '' || (err.json().path && err.json().path.indexOf('/api/account') === 0))*/
                    ) {
                        this.eventManager.broadcast({
                            name: 'coetlApp.httpError',
                            content: err.error
                        });
                    }
                    return Observable.throw(err.error);
                })
            );
    }
}
