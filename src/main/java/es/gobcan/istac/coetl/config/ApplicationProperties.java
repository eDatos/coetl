package es.gobcan.istac.coetl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Installation installation = new Installation();
    private final Jobs jobs = new Jobs();

    public Installation getInstallation() {
        return installation;
    }

    public static class Installation {

        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
    
    public Jobs getJobs() {
        return this.jobs;
    }
    
    public static class Jobs {
        
        private Cron cron = new Cron();

        
        public Cron getCron() {
            return cron;
        }

        public void setCron(Cron cron) {
            this.cron = cron;
        }
    }
    
    public static class Cron {
        
        private String enabledTokens;
        
        public String getEnabledTokens() {
            return enabledTokens;
        }
        
        public void setEnabledTokens(String enabledTokens) {
            this.enabledTokens = enabledTokens;
        }
    }
}
