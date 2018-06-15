import { DatePipe } from '@angular/common/src/pipes';

import { BaseEntityFilter, BatchSelection, EntityFilter, HasBatchOperations } from '../../../shared';
import { Actor } from '../../actor/actor.model';
import { Categoria } from '../../categoria/categoria.model';
import { Idioma } from '../../idioma/idioma.model';
import { ActorService } from '../../actor';
import { IdiomaService } from '../../idioma';
import { CategoriaService } from '../../categoria';

export class PeliculaFilter extends BaseEntityFilter implements EntityFilter, HasBatchOperations {

    public allIdiomas: Idioma[] = [];
    public allActores: Actor[] = [];
    public allCategorias: Categoria[] = [];

    public titulo: string;
    public fechaEstreno: Date;
    public idioma: any;
    public categorias: any[];
    public actores: Actor[] = [];

    public batchSelection = new BatchSelection();

    constructor(
        public datePipe?: DatePipe,
        private actorService?: ActorService,
        private categoriaService?: CategoriaService,
        private idiomaService?: IdiomaService
    ) {
        super(datePipe);
    }

    fromQueryParams(params: any) {
        if (params['titulo']) {
            this.titulo = params['titulo'];
        }
        if (params['fechaEstreno']) {
            this.fechaEstreno = params['fechaEstreno'];
        }
        if (params['idioma']) {
            this.idioma = this.allIdiomas.find((idioma) => idioma.id === Number(params['idioma']));
        }
        if (params['categorias']) {
            this.categorias = params['categorias'].split(',')
                .map((searchId) => Number(searchId))
                .map((searchId) => this.allCategorias.find((categoria) => categoria.id === searchId))
                .filter((categoria) => !!categoria);
        }
    }

    protected registerAsyncParameters() {
        this.registerAsyncParam({
            paramName: 'actores',
            entityProperty: 'id',
            collectionName: 'allActores',
            parseFromString: (item: string) => Number(item),
            load: (actorIdList, notifyDone) => {
                this.actorService.query({ query: `ID IN (${actorIdList.join(',')})` }).subscribe((res) => {
                    this.allActores = res.json;
                    this.actores = res.json;
                    notifyDone();
                });
            }
        });
    }

    reset() {
        this.titulo = null;
        this.fechaEstreno = null;
        this.idioma = null;
        this.categorias = null;
        this.actores = null;
    }

    toQueryForBatch(query?: string) {
        if (!query) { query = this.toQuery(); }
        return [
            query,
            this.batchSelection.toQuery()
        ].filter((value) => !!value).join(' AND ');
    }

    toUrl(queryParams) {
        const obj = Object.assign({}, queryParams);
        this.updateQueryParam('titulo', obj);
        this.updateQueryParam('fechaEstreno', obj);
        this.updateQueryParam('idioma', obj);
        this.updateQueryParam('categorias', obj);
        this.updateQueryParam('actores', obj);
        return obj;
    }

    getCriterias() {
        const criterias = [];
        if (this.titulo) {
            criterias.push(`TITULO ILIKE '%${this.titulo}%'`);
        }
        if (this.fechaEstreno) {
            criterias.push(`FECHAESTRENO GE '${this.dateToString(this.fechaEstreno)}'`);
        }
        if (this.idioma) {
            criterias.push(`IDIOMA EQ '${this.idioma.id}'`);
        }
        if (this.categorias && this.categorias.length > 0) {
            criterias.push(`CATEGORIAS IN (${this.categorias.map((categoria) => categoria.id).join(',')})`);
        }
        if (this.actores && this.actores.length > 0) {
            criterias.push(`ACTORES IN (${this.actores.map((actor) => actor.id).join(',')})`);
        }

        return criterias;
    }
}
