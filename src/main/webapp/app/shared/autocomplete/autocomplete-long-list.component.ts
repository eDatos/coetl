import { Component, OnInit } from '@angular/core';
import { AutocompleteComponent } from '.';
import { buildProvider } from '..';

/*
  Documentación en http://confluence.arte-consultores.com/display/INFRASTR/ac-autocomplete-long-list
*/
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

        this.minLength = 3;
        super.ngOnInit();
    }
}
