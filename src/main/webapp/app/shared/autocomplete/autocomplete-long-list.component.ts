import { Component, OnInit } from '@angular/core';
import { AutocompleteComponent } from '.';
import { buildProvider } from '..';
import { TranslateService } from '@ngx-translate/core';

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

    constructor(protected translateService: TranslateService) {
        super(translateService);
        this.minLength = 3;
     }

    ngOnInit() {
        if (!(this.completeMethod.observers.length > 0)) {
            throw new Error('completeMethod is required on ac-autocomplete-long-list');
        }
        this.placeholder = this.translateService.instant('entity.list.empty.writeForSuggestions');
        super.ngOnInit();
    }
}
