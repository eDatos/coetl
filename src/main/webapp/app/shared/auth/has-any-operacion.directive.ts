import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { Principal } from './principal.service';

/**
 * @whatItDoes Conditionally includes an HTML element if current user has any
 * of the roles passed as the `expression`.
 *
 * @howToUse
 * ```
 *     <some-element *acHasAnyOperacion="'ROLE_ADMIN'">...</some-element>
 *
 *     <some-element *acHasAnyOperacion="['ROLE_ADMIN', 'ROLE_USER']">...</some-element>
 * ```
 */
@Directive({
    selector: '[acHasAnyOperacion]'
})
export class HasAnyOperacionDirective {

    private operaciones: string[];

    constructor(private principal: Principal, private templateRef: TemplateRef<any>, private viewContainerRef: ViewContainerRef) {
    }

    @Input()
    set acHasAnyOperacion(value: string | string[]) {
        this.operaciones = typeof value === 'string' ? [<string>value] : <string[]>value;
        this.updateView();
        // Get notified each time authentication state changes.
        this.principal.getAuthenticationState().subscribe((identity) => this.updateView());
    }

    private updateView(): void {
        this.principal.canDoAnyOperacion(this.operaciones).then((result) => {
            this.viewContainerRef.clear();
            if (result) {
                this.viewContainerRef.createEmbeddedView(this.templateRef);
            }
        });
    }
}
