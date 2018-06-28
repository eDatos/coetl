import { Injectable } from '@angular/core';
import { Principal, Rol } from '../../shared';

export const ACTOR_ROLES = [Rol.USER];

@Injectable()
export class ActorPermissionService {

    constructor(
        private principal: Principal
    ) { }

    puedeNavegarActor(): boolean {
        return this.principal.rolesRutaMatchesRolesUsuario(ACTOR_ROLES);
    }
}
