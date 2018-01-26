// FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)

import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ArteApplicationTemplateOperacionModule } from './operacion/operacion.module';
import { ArteApplicationTemplatePeliculaModule } from './pelicula/pelicula.module';
import { ArteApplicationTemplateActorModule } from './actor/actor.module';
import { ArteApplicationTemplateCategoriaModule } from './categoria/categoria.module';
import { ArteApplicationTemplateIdiomaModule } from './idioma/idioma.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ArteApplicationTemplateOperacionModule,
        ArteApplicationTemplatePeliculaModule,
        ArteApplicationTemplateActorModule,
        ArteApplicationTemplateCategoriaModule,
        ArteApplicationTemplateIdiomaModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArteApplicationTemplateEntityModule {}
