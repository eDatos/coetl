package es.gobcan.coetl.optimistic;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        AppContext.setApplicationContext(applicationContext);
    }

    public static class AppContext {

        private static ApplicationContext ctx;

        private AppContext() {
        }

        public static void setApplicationContext(ApplicationContext applicationContext) {
            ctx = applicationContext;
        }

        public static ApplicationContext getApplicationContext() {
            return ctx;
        }
    }
}