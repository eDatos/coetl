package es.gobcan.istac.coetl.service.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import es.gobcan.istac.coetl.domain.Health;
import es.gobcan.istac.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.istac.coetl.errors.ErrorConstants;

@Component
public class HealthValidator extends AbstractValidator<Health> {

    private static final String FIELD_IS_BLANK_ERROR_MESSAGE = "The field %s can not be blank.";

    @Override
    public void validate(Health entity) {
        checkServiceNameIsNotBlank(entity);
        checkEndpointIsNotBlank(entity);
    }

    private void checkServiceNameIsNotBlank(Health entity) {
        if (StringUtils.isBlank(entity.getServiceName())) {
            throw new CustomParameterizedExceptionBuilder().message(String.format(FIELD_IS_BLANK_ERROR_MESSAGE, "serviceName")).code(ErrorConstants.HEALTH_SERVICE_NAME_IS_BLANK).build();
        }
    }

    private void checkEndpointIsNotBlank(Health entity) {
        if (StringUtils.isBlank(entity.getEndpoint())) {
            throw new CustomParameterizedExceptionBuilder().message(String.format(FIELD_IS_BLANK_ERROR_MESSAGE, "endpoint")).code(ErrorConstants.HEALTH_ENDPOINT_IS_BLANK).build();
        }
    }
}