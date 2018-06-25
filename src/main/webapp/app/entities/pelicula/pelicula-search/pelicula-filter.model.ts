import { DatePipe } from '@angular/common/src/pipes';

import { BaseEntityFilter, BatchSelection, EntityFilter, HasBatchOperations } from '../../../shared';
import { Actor } from '../../actor/actor.model';
import { Categoria } from '../../categoria/categoria.model';
import { Idioma } from '../../idioma/idioma.model';
import { ActorService } from '../../actor';

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
    ) {
        super(datePipe);
    }

    protected registerParameters() {
        this.registerParam({
            paramName: 'titulo',
            updateModel: (param) => this.titulo = param,
            clear: () => this.titulo = null
        });

        this.registerParam({
            paramName: 'fechaEstreno',
            updateModel: (param) => this.fechaEstreno = param,
            clear: () => this.fechaEstreno = null
        });

        this.registerParam({
            paramName: 'idioma',
            updateModel: (param) => this.idioma = this.allIdiomas.find((idioma) => idioma.id === Number(param)),
            clear: () => this.idioma = null
        });

        this.registerParam({
            paramName: 'categorias',
            updateModel: (param) => {
                this.categorias = param.split(',')
                    .map((searchId) => Number(searchId))
                    .map((searchId) => this.allCategorias.find((categoria) => categoria.id === searchId))
                    .filter((categoria) => !!categoria)
            },
            clear: () => this.categorias = null
        });

        this.registerParam({
            paramName: 'actores',
            updateModel: (param) => {
                this.actores = param.split(',')
                    .map((searchId) => Number(searchId))
                    .map((searchId) => this.allActores.find((actor) => actor.id === searchId))
                    .filter((actor) => !!actor);
            },
            clear: () => this.actores = null,
            createSubscription: (param) => this.actorService.query({ query: `ID IN (${param})` }),
            callback: (response) => {
                this.allActores = response.json;
                this.actores = response.json;
            },
            selectedIdsAreInSuggestions: (param) => {
                return param.split(',').every((paramElement) => {
                    return this.allActores.find((suggestion) => suggestion['id'] === Number(paramElement)) !== undefined;
                }, this);
            }
        });
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
