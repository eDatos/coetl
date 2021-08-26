import { Injectable } from '@angular/core';
import { Principal } from './principal.service';
import { Rol } from './rol.model';

export const USER_MANAGEMENT_ROLES = [Rol.ADMINISTRADOR];
export const HERRAMIENTAS_ROLES = [Rol.ADMINISTRADOR];
export const READ_ETL_ROLES = [Rol.ADMINISTRADOR, Rol.TECNICO_PRODUCCION, Rol.LECTOR];
export const MANAGE_ETL_ROLES = [Rol.ADMINISTRADOR, Rol.TECNICO_PRODUCCION];

@Injectable()
export class PermissionService {
    constructor(private principal: Principal) {}

    // TODO: ELiminar este permiso
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
