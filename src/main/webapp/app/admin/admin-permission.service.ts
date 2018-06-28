import { Injectable } from '@angular/core';
import { Principal, Rol } from '../shared';

export const USER_MNGMT_ROLES = [Rol.ADMIN];
export const HERRAMIENTAS_ROLES = [Rol.ADMIN];

@Injectable()
export class AdminPermissionService {

    constructor(
        private principal: Principal
    ) { }

    puedeNavegarUserManagement(): boolean {
        return this.principal.rolesRutaMatchesRolesUsuario(USER_MNGMT_ROLES);
    }

    puedeNavegarHerramientas(): boolean {
        return this.principal.rolesRutaMatchesRolesUsuario(HERRAMIENTAS_ROLES);
    }
}
