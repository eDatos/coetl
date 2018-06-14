import { ParamLoader } from './param-loader';

export class FilterHelper {

    private count = 0;

    constructor(
        private observer: any,
        private loaders: ParamLoader[]
    ) { }

    notifyDone() {
        this.count++;
        if (this.loaders.length === this.count) {
            this.observer.next();
            this.observer.complete();
        }
    }
}
