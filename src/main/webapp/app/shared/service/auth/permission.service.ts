import { Injectable } from '@angular/core';
import { Principal } from './principal.service';
import { Rol } from './rol.model';

export const USER_MANAGEMENT_ROLES = [Rol.ADMIN];
export const HERRAMIENTAS_ROLES = [Rol.ADMIN];
export const READ_ETL_ROLES = [Rol.ADMIN, Rol.TECNICO, Rol.LECTOR];
export const MANAGE_ETL_ROLES = [Rol.ADMIN, Rol.TECNICO];

@Injectable()
export class PermissionService {
    constructor(private principal: Principal) {}

    puedeNavegarUserManagement(): boolean {
        return this.principal.rolesRutaMatchesRolesUsuario(USER_MANAGEMENT_ROLES);
    }

    puedeNavegarHerramientas(): boolean {
        return this.principal.rolesRutaMatchesRolesUsuario(HERRAMIENTAS_ROLES);
    }

    canReadEtl(): boolean {
        return this.principal.rolesRutaMatchesRolesUsuario(READ_ETL_ROLES);
    }

    canManageEtl(): boolean {
        return this.principal.rolesRutaMatchesRolesUsuario(MANAGE_ETL_ROLES);
    }
}
