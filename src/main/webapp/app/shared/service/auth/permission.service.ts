import { Injectable } from '@angular/core';
import { Principal } from './principal.service';
import { Rol } from './rol.model';

export const USER_MANAGEMENT_ROLES = [Rol.ADMIN];
export const HERRAMIENTAS_ROLES = [Rol.ADMIN];

@Injectable()
export class PermissionService {
    constructor(private principal: Principal) {}

    puedeNavegarUserManagement(): boolean {
        return this.principal.rolesRutaMatchesRolesUsuario(USER_MANAGEMENT_ROLES);
    }

    puedeNavegarHerramientas(): boolean {
        return this.principal.rolesRutaMatchesRolesUsuario(HERRAMIENTAS_ROLES);
    }
}
