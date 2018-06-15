import { Executable } from './executable';
import { Observable } from 'rxjs/Rx';
import { ExecuterHelper } from './executer-helper';

export function execute(executables: Executable[]): Observable<void> {
    return Observable.create((observer) => {
        if (executables.length === 0) {
            observer.next();
            observer.complete();
            return;
        }

        const helper = new ExecuterHelper(observer, executables.length);
        executables.forEach((executable) => executable(helper.notifyDone.bind(helper)));
    });
}
