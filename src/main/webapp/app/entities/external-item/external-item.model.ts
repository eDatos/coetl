import { BaseVersionedEntity } from '../../shared';

export enum TypeExternalArtefacts {
    STATISTICAL_OPERATION = 'STATISTICAL_OPERATION'
}

export class ExternalItem extends BaseVersionedEntity {
    public id?: number;
    public code?: string;
    public urn?: string;

    public name?: string;

    public type?: TypeExternalArtefacts;
}

export class StadisticalOperation extends ExternalItem {
    public enabled = false;
    public publicationDate?: Date;

    constructor() {
        super();
        this.type = TypeExternalArtefacts.STATISTICAL_OPERATION;
    }
}
