export class BatchSelection {
    allSelected: Boolean = false;
    selectedIds: number[] = [];

    hasSelection(): boolean {
        return this.selectedIds.length > 0;
    }

    toQuery(): string {
        return this.getCriterias().join(' AND ');
    }

    toggleIds(visibleIds: number[]) {
        if (this.allVisibleIdsAreSelected(visibleIds)) {
            this.removeIds(visibleIds);
        } else {
            this.addIds(visibleIds);
        }
    }

    removeIds(ids: number[]) {
        this.selectedIds = this.selectedIds.filter((id) => ids.indexOf(id) === -1);
    }

    // Add without duplicates
    addIds(ids: number[]) {
        const newSelectedIds = [];
        this.selectedIds.concat(ids).map((id) => {
            if (newSelectedIds.indexOf(id) === -1) {
                newSelectedIds.push(id);
            }
        });
        this.selectedIds = newSelectedIds;
    }

    allVisibleIdsAreSelected(visibleIds: number[]): boolean {
        // Comprueba si todos los elementos visibles estan seleccionados
        return visibleIds.every((id) => this.selectedIds.indexOf(id) > -1);
    }

    getCriterias(): string[] {
        const criterias = [];
        if (this.selectedIds && this.selectedIds.length) {
            criterias.push(`ID IN (${this.selectedIds.join(',')})`);
        }
        return criterias;
    }
};
