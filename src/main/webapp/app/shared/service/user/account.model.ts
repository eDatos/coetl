import { Rol } from '../auth/rol.model';
import * as jwtDecode from 'jwt-decode';
import { ExternalItem } from '../../../entities/external-item';

export class Account {
    constructor(
        public activated: boolean,
        public roles: Rol[],
        public email: string,
        public nombre: string,
        public idioma: string,
        public apellido1: string,
        public apellido2: string,
        public login: string
    ) {}
}

export interface RolCAS {
    app: string;
    role: string;
    operation?: string;
}

export class UserCAS {
    private readonly ACL_APP_NAME: string = 'GESTOR_CONSOLA_ETL';

    public static fromJwt(token: string) {
        const payload: { sub: string; auth: string; exp: string } = jwtDecode(token);
        const rolesCas = payload.auth.split(',').map((appRole) => {
            const [app, role, operation] = appRole.split('#', 3);
            return { app, role, operation } as RolCAS;
        });
        return new UserCAS(payload.sub, rolesCas);
    }

    constructor(public login: string, public roles: RolCAS[]) {}

    public hasRole(rol: Rol, operation?: ExternalItem): boolean {
        if (operation != null && !this.isAdminRole(rol)) {
            return this.roles.some(
                (userRol) =>
                    userRol.app === this.ACL_APP_NAME &&
                    userRol.role === rol &&
                    userRol.operation === operation.code
            );
        }
        return this.roles.some(
            (userRol) =>
                userRol.app === this.ACL_APP_NAME && userRol.role === rol && !userRol.operation
        );
    }

    private isAdminRole(rol: Rol): boolean {
        return this.roles.some(
            (userRol) => userRol.app === this.ACL_APP_NAME && userRol.role === Rol.ADMINISTRADOR
        );
    }
}
