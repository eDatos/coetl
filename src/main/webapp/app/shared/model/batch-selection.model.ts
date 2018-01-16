export class BatchSelection {
    allSelected: Boolean = false;
    selectedIds: number[] = [];

    hasSelection() {
        return this.allSelected || this.selectedIds.length > 0;
    }

    toQuery() {
        return this.getCriterias().join(' AND ');
    }

    getCriterias() {
        const criterias = [];
        if (!this.allSelected) {
            if (this.selectedIds && this.selectedIds.length) {
                criterias.push(`ID IN (${this.selectedIds.join(',')})`);
            }
        }
        return criterias;
    }
};
