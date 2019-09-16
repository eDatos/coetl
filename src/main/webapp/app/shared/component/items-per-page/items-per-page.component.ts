import { Component, EventEmitter, forwardRef, Input, Output } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { ITEMS_PER_PAGE, PAGINATION_OPTIONS } from './items-per-page.constants';

export const AC_PAGINATION_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => ItemsPerPageComponent),
    multi: true
};

@Component({
    selector: 'ac-items-per-page',
    templateUrl: 'items-per-page.component.html',
    styleUrls: ['items-per-page.component.scss'],
    providers: [AC_PAGINATION_VALUE_ACCESSOR]
})
export class ItemsPerPageComponent {
    @Input()
    options = PAGINATION_OPTIONS;

    _itemsPerPage = ITEMS_PER_PAGE;

    @Output()
    itemsPerPageChange: EventEmitter<number> = new EventEmitter<number>();

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
