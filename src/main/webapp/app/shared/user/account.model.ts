export class Account {
    constructor(
        public activated: boolean,
        public authorities: string[],
        public email: string,
        public nombre: string,
        public idioma: string,
        public apellidos: string,
        public login: string,
        public urlImage: string
    ) { }
}
