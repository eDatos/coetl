import { Component, OnInit } from '@angular/core';
import { AutocompleteComponent } from '.';
import { buildProvider } from '..';

@Component({
    selector: 'ac-autocomplete-short-list',
    templateUrl: 'autocomplete.component.html',
    styleUrls: ['autocomplete.component.scss'],
    providers: [buildProvider(AutocompleteShortListComponent)]
})
export class AutocompleteShortListComponent extends AutocompleteComponent implements OnInit {

    ngOnInit() {
        if (this.completeMethod.observers.length > 0) {
            throw new Error('completeMethod is not supported on ac-autocomplete-short-list');
        }

        super.ngOnInit();
    }
}
