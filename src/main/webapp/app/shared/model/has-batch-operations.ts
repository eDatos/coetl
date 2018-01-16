import { BatchSelection } from './batch-selection.model';

export interface HasBatchOperations {
    toQueryForBatch(query: string): string;
    batchSelection: BatchSelection;
}
