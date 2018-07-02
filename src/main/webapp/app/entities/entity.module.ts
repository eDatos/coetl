// FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { ArteApplicationTemplateActorModule } from './actor/actor.module';
import { ArteApplicationTemplateCategoriaModule } from './categoria/categoria.module';
import { ArteApplicationTemplateDocumentoModule } from './documento/documento.module';
import { ArteApplicationTemplateIdiomaModule } from './idioma/idioma.module';
import { ArteApplicationTemplatePeliculaModule } from './pelicula/pelicula.module';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ArteApplicationTemplatePeliculaModule,
        ArteApplicationTemplateActorModule,
        ArteApplicationTemplateCategoriaModule,
        ArteApplicationTemplateIdiomaModule,
        ArteApplicationTemplateDocumentoModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArteApplicationTemplateEntityModule {}
