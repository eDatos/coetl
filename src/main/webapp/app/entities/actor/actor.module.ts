import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ArteApplicationTemplateSharedModule } from '../../shared';
import {
    ActorComponent,
    ActorDialogComponent,
    ActorPopupComponent,
    actorPopupRoute,
    ActorPopupService,
    ActorResolvePagingParams,
    actorRoute,
    ActorService
} from '.';

const ENTITY_STATES = [
    ...actorRoute,
    ...actorPopupRoute,
];

@NgModule({
    imports: [
        ArteApplicationTemplateSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ActorComponent,
        ActorDialogComponent,
        ActorPopupComponent
    ],
    entryComponents: [
        ActorComponent,
        ActorDialogComponent,
        ActorPopupComponent
    ],
    providers: [
        ActorService,
        ActorPopupService,
        ActorResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArteApplicationTemplateActorModule {}
