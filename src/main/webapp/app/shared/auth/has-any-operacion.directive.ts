import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { Principal } from './principal.service';
import { Rol } from '../rol/rol.model';

/**
 * @whatItDoes Conditionally includes an HTML element if current user has any
 * of the roles passed as the `expression`.
 *
 * @howToUse
 * ```
 *     <some-element *acHasAnyOperacion="'ADMIN'">...</some-element>
 *
 *     <some-element *acHasAnyOperacion="['ADMIN', 'READ_ONLY_USER']">...</some-element>
 * ```
 */
@Directive({
    selector: '[acHasAnyOperacion]'
})
export class HasAnyOperacionDirective {

    private roles: string[];

    constructor(
        private principal: Principal,
        private templateRef: TemplateRef<any>,
        private viewContainerRef: ViewContainerRef
    ) { }

    @Input()
    set acHasAnyOperacion(value: string | string[]) {
        this.roles = typeof value === 'string' ? [<string>value] : <string[]>value;
        this.updateView();
        // Get notified each time authentication state changes.
        this.principal.getAuthenticationState().subscribe((identity) => this.updateView());
    }

    private updateView(): void {
        this.principal.hasRoles(this.rolFromString(this.roles)).then((result) => {
            this.viewContainerRef.clear();
            if (result) {
                this.viewContainerRef.createEmbeddedView(this.templateRef);
            }
        });
    }

    private rolFromString(roles: any[]): Rol[] {
        if (typeof roles === 'string') {
            roles = [roles];
        }
        if (roles && roles.length > 0) {
            roles = roles.map((stringRol) => Object.keys(Rol).find((objectRol) => objectRol.toUpperCase() === stringRol.toUpperCase()))
                .filter((rol) => !!rol);
        }
        return roles;
    }
}
