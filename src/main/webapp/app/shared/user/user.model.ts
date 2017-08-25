export class User {
    public id?: any;
    public login?: string;
    public nombre?: string;
    public apellidos?: string;
    public email?: string;
    public activo?: Boolean;
    public idioma?: string;
    public roles?: any[];
    public createdBy?: string;
    public createdDate?: Date;
    public lastModifiedBy?: string;
    public lastModifiedDate?: Date;

    constructor(
        id?: any,
        login?: string,
        nombre?: string,
        apellidos?: string,
        email?: string,
        activo?: Boolean,
        idioma?: string,
        roles?: any[],
        createdBy?: string,
        createdDate?: Date,
        lastModifiedBy?: string,
        lastModifiedDate?: Date,
    ) {
        this.id = id ? id : null;
        this.login = login ? login : null;
        this.nombre = nombre ? nombre : null;
        this.apellidos = apellidos ? apellidos : null;
        this.email = email ? email : null;
        this.activo = activo ? activo : false;
        this.idioma = idioma ? idioma : null;
        this.roles = roles ? roles : null;
        this.createdBy = createdBy ? createdBy : null;
        this.createdDate = createdDate ? createdDate : null;
        this.lastModifiedBy = lastModifiedBy ? lastModifiedBy : null;
        this.lastModifiedDate = lastModifiedDate ? lastModifiedDate : null;
    }
}
