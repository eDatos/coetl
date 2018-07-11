import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ArteApplicationTemplateSharedModule } from '../../shared';
import {
    PeliculaBatchDeleteDialogComponent,
    PeliculaComponent,
    PeliculaDeleteDialogComponent,
    PeliculaDeletePopupComponent,
    PeliculaFormComponent,
    peliculaPopupRoute,
    PeliculaPopupService,
    PeliculaResolve,
    PeliculaResolvePagingParams,
    peliculaRoute,
    PeliculaService
} from '.';
import { PeliculaSearchComponent } from './pelicula-search';

const ENTITY_STATES = [
    ...peliculaRoute,
    ...peliculaPopupRoute,
];

@NgModule({
    imports: [
        ArteApplicationTemplateSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PeliculaComponent,
        PeliculaFormComponent,
        PeliculaDeleteDialogComponent,
        PeliculaDeletePopupComponent,
        PeliculaBatchDeleteDialogComponent,
        PeliculaSearchComponent,
    ],
    entryComponents: [
        PeliculaComponent,
        PeliculaFormComponent,
        PeliculaDeleteDialogComponent,
        PeliculaDeletePopupComponent,
        PeliculaBatchDeleteDialogComponent,
        PeliculaSearchComponent,
    ],
    providers: [
        PeliculaService,
        PeliculaPopupService,
        PeliculaResolvePagingParams,
        PeliculaResolve
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArteApplicationTemplatePeliculaModule {}
