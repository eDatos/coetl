// FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)

import { platformBrowser } from '@angular/platform-browser';
import { ProdConfig } from './blocks/config/prod.config';
import { ArteApplicationTemplateAppModuleNgFactory } from '../../../../target/aot/src/main/webapp/app/app.module.ngfactory';

ProdConfig();

platformBrowser().bootstrapModuleFactory(ArteApplicationTemplateAppModuleNgFactory)
.then((success) => console.log(`Application started`))
.catch((err) => console.error(err));
