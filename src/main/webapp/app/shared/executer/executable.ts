export interface Executable {
    (notifyDone: () => void): void;
}
