export class Account {
    constructor(
        public activated: boolean,
        public roles: string[],
        public email: string,
        public nombre: string,
        public idioma: string,
        public apellido1: string,
        public apellido2: string,
        public login: string,
        public urlImage: string
    ) { }
}
