export interface ParamLoader {
    paramName: string;
    entityProperty: string;
    collectionName: string;
    parseFromString: (item: string) => any;
    load: (params: any[], notifyDone: () => void) => void;
}
