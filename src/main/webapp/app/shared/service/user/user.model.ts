import { BaseAuditingEntity } from '../../model/base-auditing-entity';

export class User extends BaseAuditingEntity {
    public id?: any;
    public login?: string;
    public nombre?: string;
    public apellido1?: string;
    public apellido2?: string;
    public email?: string;
    public roles?: any[];

    constructor(
        id?: any,
        login?: string,
        nombre?: string,
        apellido1?: string,
        apellido2?: string,
        email?: string,
        roles?: any[]
    ) {
        super();
        this.id = id ? id : null;
        this.login = login ? login : null;
        this.nombre = nombre ? nombre : null;
        this.apellido1 = apellido1 ? apellido1 : null;
        this.apellido2 = apellido2 ? apellido2 : null;
        this.email = email ? email : null;
        this.roles = roles ? roles : null;
    }
}
