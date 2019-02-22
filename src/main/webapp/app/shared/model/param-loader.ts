import { Observable } from 'rxjs';
export interface ParamLoader {
    paramName: string;
    updateFilterFromParam: (param: any) => void;
    clearFilter: () => void;
    
    // Async loaders
    recoverFilterFromServer?: (param: any) => Observable<any>;
    updateFilterAndSuggestionsFromServer?: (response) => void;
    needsToRecoverFilterFromServer?: (param: string) => boolean;
}
