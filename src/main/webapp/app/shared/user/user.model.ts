export class User {
    public id?: any;
    public login?: string;
    public nombre?: string;
    public apellido1?: string;
    public apellido2?: string;
    public email?: string;
    public roles?: any[];
    public createdBy?: string;
    public createdDate?: Date;
    public lastModifiedBy?: string;
    public lastModifiedDate?: Date;
    public deletionDate?: Date;

    constructor(
        id?: any,
        login?: string,
        nombre?: string,
        apellido1?: string,
        apellido2?: string,
        email?: string,
        roles?: any[],
        createdBy?: string,
        createdDate?: Date,
        lastModifiedBy?: string,
        lastModifiedDate?: Date,
        deletionDate?: Date,
    ) {
        this.id = id ? id : null;
        this.login = login ? login : null;
        this.nombre = nombre ? nombre : null;
        this.apellido1 = apellido1 ? apellido1 : null;
        this.apellido2 = apellido2 ? apellido2 : null;
        this.email = email ? email : null;
        this.roles = roles ? roles : null;
        this.createdBy = createdBy ? createdBy : null;
        this.createdDate = createdDate ? createdDate : null;
        this.lastModifiedBy = lastModifiedBy ? lastModifiedBy : null;
        this.lastModifiedDate = lastModifiedDate ? lastModifiedDate : null;
        this.deletionDate = deletionDate ? deletionDate : null;
    }
}
