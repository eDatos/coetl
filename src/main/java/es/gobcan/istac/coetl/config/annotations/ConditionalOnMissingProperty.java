package es.gobcan.istac.coetl.config.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;


@Retention(RUNTIME)
@Target({TYPE, METHOD})
@Conditional(ConditionalOnMissingProperty.MissingPropertyCondition.class)
public @interface ConditionalOnMissingProperty {

    String PROPERTY_KEYS = "propertyKeys";

    @AliasFor(PROPERTY_KEYS)
    String[] value() default {};
    
    String prefix() default "";

    @AliasFor("value")
    String[] propertyKeys() default {};
    
    class MissingPropertyCondition extends SpringBootCondition {

        @Override
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            String[] keys = (String[]) metadata.getAnnotationAttributes(ConditionalOnMissingProperty.class.getName()).get(PROPERTY_KEYS);
            String prefix = (String) metadata.getAnnotationAttributes(ConditionalOnMissingProperty.class.getName()).get("prefix");
            if (keys.length > 0) {
                boolean allMissing = true;
                for (String key : keys) {
                    String propertyValue = context.getEnvironment().getProperty(prefix + "." + key);
                    allMissing &= StringUtils.isEmpty(propertyValue);
                }
                if (allMissing) {
                    return new ConditionOutcome(true, "The following properties were all null or empty in the environment: " + Arrays.toString(keys));
                }
                return new ConditionOutcome(false, "one or more properties were found.");
            } else {
                throw new RuntimeException("expected method annotated with " + ConditionalOnMissingProperty.class.getName() + " to include a non-empty " + PROPERTY_KEYS + " attribute");
            }
        }
        
    }
}
