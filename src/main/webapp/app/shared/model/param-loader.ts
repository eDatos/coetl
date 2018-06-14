export interface ParamLoader {
    (params: any, notifyDone: () => void): void;
}
