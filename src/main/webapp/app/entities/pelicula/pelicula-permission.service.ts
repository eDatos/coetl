import { Injectable } from '@angular/core';
import { Principal, Rol } from '../../shared';

export const PELICULA_ROLES = [Rol.USER];

@Injectable()
export class PeliculaPermissionService {

    constructor(
        private principal: Principal
    ) { }

    puedeNavegarPelicula(): boolean {
        return this.principal.rolesRutaMatchesRolesUsuario(PELICULA_ROLES);
    }
}
