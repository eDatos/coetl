import { Component, EventEmitter, forwardRef, Input, Output } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';

import { ITEMS_PER_PAGE, PAGINATION_OPTIONS } from '..';

export const AC_PAGINATION_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => PaginationComponent),
    multi: true
};

@Component({
    selector: 'ac-pagination',
    templateUrl: 'pagination.component.html',
    styleUrls: ['pagination.component.scss'],
    providers: [AC_PAGINATION_VALUE_ACCESSOR]

})
export class PaginationComponent {

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
