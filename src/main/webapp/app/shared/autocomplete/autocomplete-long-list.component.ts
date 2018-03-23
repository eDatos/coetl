import { Component, OnInit } from '@angular/core';
import { AutocompleteComponent } from '.';
import { buildProvider } from '..';

@Component({
    selector: 'ac-autocomplete-long-list',
    templateUrl: 'autocomplete.component.html',
    styleUrls: ['autocomplete.component.scss'],
    providers: [buildProvider(AutocompleteLongListComponent)]
})
export class AutocompleteLongListComponent extends AutocompleteComponent implements OnInit {

    ngOnInit() {
        if (!(this.completeMethod.observers.length > 0)) {
            throw new Error('completeMethod is required on ac-autocomplete-long-list');
        }
        if (typeof this.propertiesToQuery !== 'undefined') {
            throw new Error('propertiesToQuery not supported on ac-autocomplete-long-list');
        }
        super.ngOnInit();
    }
}
