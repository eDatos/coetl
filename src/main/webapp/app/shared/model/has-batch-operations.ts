import { BatchSelection } from './batch-selection.model';

export interface HasBatchOperations {
    batchSelection: BatchSelection;
    toQueryForBatch(query: string): string;
}
