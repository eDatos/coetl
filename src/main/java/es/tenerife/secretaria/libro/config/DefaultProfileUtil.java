package es.tenerife.secretaria.libro.config;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import io.github.jhipster.config.JHipsterConstants;

/**
 * Utility class to load a Spring profile to be used as default
 * when there is no <code>spring.profiles.active</code> set in the environment or as command line argument.
 * If the value is not available in <code>application.yml</code> then <code>dev</code> profile will be used as default.
 */
public final class DefaultProfileUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProfileUtil.class);
	
	private static final String DATA_LOCATION_CONFIG = "config/data-location.properties";
    private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";

    private DefaultProfileUtil() {
    }

    private static Properties createDefaultProperties() {
        Properties props = new Properties();
        try {
            Resource resource = new ClassPathResource(DATA_LOCATION_CONFIG);
            props = PropertiesLoaderUtils.loadProperties(resource);
            return props;
        } catch (IOException e) {
            LOGGER.debug("Unable to load resource specified in data-location",e);
        }
        return props;
    }
    
    /**
     * Set a default to use when no profile is configured.
     *
     * @param app the Spring application
     */
    public static void addDefaultProfile(SpringApplication app) {
    	Properties defProperties = createDefaultProperties();
    	
        /*
        * The default profile to use when no other profiles are defined
        * This cannot be set in the <code>application.yml</code> file.
        * See https://github.com/spring-projects/spring-boot/issues/1219
        */
        defProperties.put(SPRING_PROFILE_DEFAULT, JHipsterConstants.SPRING_PROFILE_DEVELOPMENT);
        
        // Add other properties defined in file data-location.properties
        defProperties.putAll(defProperties);
        
        app.setDefaultProperties(defProperties);
    }

    /**
     * Get the profiles that are applied else get default profiles.
     *
     * @param env spring environment
     * @return profiles
     */
    public static String[] getActiveProfiles(Environment env) {
        String[] profiles = env.getActiveProfiles();
        if (profiles.length == 0) {
            return env.getDefaultProfiles();
        }
        return profiles;
    }
}
