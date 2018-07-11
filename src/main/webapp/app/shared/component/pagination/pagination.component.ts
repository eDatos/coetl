import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
    selector: 'ac-pagination',
    templateUrl: 'pagination.component.html'
})
export class PaginationComponent {

    public _page: number;

    @Output()
    pageChange: EventEmitter<number> = new EventEmitter<number>();

    public _totalItems: number;

    @Output()
    totalItemsChange: EventEmitter<number> = new EventEmitter<number>();

    public _itemsPerPage: number;

    @Output()
    itemsPerPageChange: EventEmitter<number> = new EventEmitter<number>();

    @Input()
    public transition: () => {};

    @Input()
    get page() {
        return this._page;
    }

    set page(page: number) {
        if (this._page !== page) {
            this._page = page;
            this.pageChange.emit(this._page);
        }
    }

    @Input()
    get totalItems() {
        return this._totalItems;
    }

    set totalItems(totalItems: number) {
        if (this._totalItems !== totalItems) {
            this._totalItems = totalItems;
            this.totalItemsChange.emit(this._totalItems);
        }
    }

    @Input()
    get itemsPerPage() {
        return this._itemsPerPage;
    }

    set itemsPerPage(itemsPerPage: number) {
        if (this._itemsPerPage !== itemsPerPage) {
            this._itemsPerPage = itemsPerPage;
            this.itemsPerPageChange.emit(this._itemsPerPage);
        }
    }
}
