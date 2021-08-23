import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute, navbarRoute, edatosNavbarRoute } from '.';

const LAYOUT_ROUTES = [navbarRoute, edatosNavbarRoute, ...errorRoute];

@NgModule({
    imports: [RouterModule.forRoot(LAYOUT_ROUTES, { useHash: true })],
    exports: [RouterModule]
})
export class LayoutRoutingModule {}
