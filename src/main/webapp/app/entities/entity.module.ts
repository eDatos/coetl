import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { CoetlActorModule } from './actor/actor.module';
import { CoetlCategoriaModule } from './categoria/categoria.module';
import { CoetlDocumentoModule } from './documento/documento.module';
import { CoetlIdiomaModule } from './idioma/idioma.module';
import { CoetlPeliculaModule } from './pelicula/pelicula.module';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        CoetlPeliculaModule,
        CoetlActorModule,
        CoetlCategoriaModule,
        CoetlIdiomaModule,
        CoetlDocumentoModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CoetlEntityModule {}
