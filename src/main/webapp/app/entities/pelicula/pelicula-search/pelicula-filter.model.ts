import { BaseEntityFilter, BatchSelection, EntityFilter, HasBatchOperations } from '../../../shared';
import { Actor } from '../../actor/actor.model';
import { Categoria } from '../../categoria/categoria.model';
import { Idioma } from '../../idioma/idioma.model';

export class SimpleFilter {
    constructor(public id?: string, public value?: string) { }
}

export class PeliculaFilter extends BaseEntityFilter implements EntityFilter, HasBatchOperations {

    public allIdiomas: Idioma[] = [];
    public allActores: Actor[] = [];
    public allCategorias: Categoria[] = [];

    public batchSelection: BatchSelection;

    constructor(
        public titulo?: string,
        public annoEstreno?: Date,
        public idioma?: any,
        public categorias?: any[],
        public actores?: any[],
    ) {
        super();
        this.batchSelection = new BatchSelection();
    }

    fromQueryParams(params: any) {
        if (params['titulo']) {
            this.titulo = params['titulo'];
        }
        if (params['idioma']) {
            this.idioma = params['idioma'].map((searchId) => Number(searchId))
                .map((searchId) => this.allIdiomas.find((idioma) => idioma.id === searchId))
                .filter((idioma) => !!idioma);
        }
        if (params['categorias']) {
            this.categorias = params['categorias'].split(',')
                .map((searchId) => Number(searchId))
                .map((searchId) => this.allCategorias.find((categoria) => categoria.id === searchId))
                .filter((categoria) => !!categoria);
        }
        if (params['actores']) {
            this.actores = params['actores'].split(',')
                .map((searchId) => Number(searchId))
                .map((searchId) => this.allActores.find((actor) => actor.id === searchId))
                .filter((actor) => !!actor);
        }
    }

    reset() {
        this.titulo = '';
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
        this.updateQueryParam('annoEstreno', obj);
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
        if (this.idioma) {
            criterias.push(`IDIOMA EQ '${this.idioma.id}'`);
        }
        if (this.categorias && this.categorias.length) {
            criterias.push(`CATEGORIA IN (${this.categorias.map((categoria) => categoria.id).join(',')})`);
        }
        if (this.actores && this.actores.length) {
            criterias.push(`ACTOR IN (${this.actores.map((actor) => actor.id).join(',')})`);
        }

        return criterias;
    }
}
