export class ExecuterHelper {

    private count = 0;

    constructor(
        private observer: any,
        private executersCount: number
    ) { }

    notifyDone() {
        this.count++;
        if (this.executersCount === this.count) {
            this.observer.next();
            this.observer.complete();
        }
    }
}
