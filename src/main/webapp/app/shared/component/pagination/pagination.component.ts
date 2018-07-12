import { Component, Input, Output, EventEmitter } from '@angular/core';
import { PAGINATION_OPTIONS, ITEMS_PER_PAGE } from '../items-per-page';

@Component({
    selector: 'ac-pagination',
    templateUrl: 'pagination.component.html'
})
export class PaginationComponent {

    public _page: number;

    @Output()
    public pageChange: EventEmitter<number> = new EventEmitter<number>();

    public _totalItems: number;

    @Output()
    public totalItemsChange: EventEmitter<number> = new EventEmitter<number>();

    public _itemsPerPage: number;

    @Output()
    public itemsPerPageChange: EventEmitter<number> = new EventEmitter<number>();

    @Output()
    public onTransition: EventEmitter<any> = new EventEmitter();

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
        const validItemsPerPage = PAGINATION_OPTIONS.indexOf(Number(itemsPerPage)) > -1 ? itemsPerPage : ITEMS_PER_PAGE
        if (this._itemsPerPage !== validItemsPerPage) {
            this._itemsPerPage = validItemsPerPage;
            this.itemsPerPageChange.emit(this._itemsPerPage);
        }
    }

    onTransitionMethod($event) {
        this.onTransition.emit($event);
    }
}
