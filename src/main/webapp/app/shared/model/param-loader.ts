import { Observable } from 'rxjs/Rx';
export interface ParamLoader {
    paramName: string;
    updateModel: (param: any) => void;
    clear: () => void;
    createSubscription?: (param: any) => Observable<any>;
    callback?: (response) => void;
    selectedIdsAreInSuggestions?: (param: string) => boolean;
}
