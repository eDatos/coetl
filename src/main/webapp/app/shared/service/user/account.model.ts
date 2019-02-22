import { Rol } from '../auth/rol.model';

export class Account {
    constructor(
        public activated: boolean,
        public roles: Rol[],
        public email: string,
        public nombre: string,
        public idioma: string,
        public apellido1: string,
        public apellido2: string,
        public login: string,
    ) { }
}
