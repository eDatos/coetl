package es.gobcan.istac.coetl.service.validator;

import es.gobcan.istac.coetl.domain.GlobalParameter;
import es.gobcan.istac.coetl.domain.Parameter;
import es.gobcan.istac.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.repository.GlobalParameterRepository;
import es.gobcan.istac.coetl.repository.ParameterRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlobalParameterValidator extends AbstractValidator<GlobalParameter> {

    private static final String FIELD_BLANK_ERROR_MESSAGE = "Field \"%s\" of Global Parameter (id=%s) can not be blank";
    private static final String FIELD_NULL_ERROR_MESSAGE = "Field \"%s\" of Global Parameter (id=%s) can not be null";
    private static final String FIELD_DUPLICATED_ERROR_MESSAGE = "Field \"%s\" of Global Parameter (id=%s) is duplicated";

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private GlobalParameterRepository globalParameterRepository;

    @Override
    public void validate(GlobalParameter entity) {
        checkKeyIsNotBlank(entity);
        checkValueIsNotBlank(entity);
        checkTypologyIsNotNull(entity);
        checkGlobalKeyIsNotDuplicated(entity);
        checkGlobalKeyIsNotDuplicatedInEtl(entity);
    }

    private void checkKeyIsNotBlank(GlobalParameter entity) {
        if (StringUtils.isBlank(entity.getKey())) {
            throw new CustomParameterizedExceptionBuilder().message(String.format(FIELD_BLANK_ERROR_MESSAGE, "key", entity.getId())).code(ErrorConstants.PARAMETER_KEY_IS_BLANK).build();
        }
    }

    private void checkValueIsNotBlank(GlobalParameter entity) {
        if (StringUtils.isBlank(entity.getValue())) {
            throw new CustomParameterizedExceptionBuilder().message(String.format(FIELD_BLANK_ERROR_MESSAGE, "value", entity.getId())).code(ErrorConstants.PARAMETER_VALUE_IS_BLANK).build();
        }
    }

    private void checkTypologyIsNotNull(GlobalParameter entity) {
        if (entity.getTypology() == null) {
            throw new CustomParameterizedExceptionBuilder().message(String.format(FIELD_NULL_ERROR_MESSAGE, "typology", entity.getId())).code(ErrorConstants.PARAMETER_EDIT).build();
        }
    }


    private void checkGlobalKeyIsNotDuplicated(GlobalParameter entity) {
        //@formatter:off
        String currentKey = entity.getKey();
        GlobalParameter duplicatedParameterKey = getOriginalEntity(status -> entity.getId() == null
            ? globalParameterRepository.findByKey(currentKey)
            : globalParameterRepository.findByKeyAndIdNot(currentKey, entity.getId()));
        //@formatter:on

        if (duplicatedParameterKey != null ) {
                throw new CustomParameterizedExceptionBuilder().message(String.format(FIELD_DUPLICATED_ERROR_MESSAGE, "key", entity.getId())).code(ErrorConstants.PARAMETER_KEY_IS_DUPLICATED_IN_GLOBAL_PARAMETER).build();
        }

    }

    private void checkGlobalKeyIsNotDuplicatedInEtl(GlobalParameter entity) {
        String currentKey = entity.getKey();
        Parameter duplicatedKey = parameterRepository.findByKey(currentKey);

        if(duplicatedKey != null){
            throw new CustomParameterizedExceptionBuilder().message(String.format(FIELD_DUPLICATED_ERROR_MESSAGE, "key", entity.getId())).code(ErrorConstants.GLOBAL_PARAMETER_KEY_IS_DUPLICATED, duplicatedKey.getEtl().getCode()).build();
        }
    }
}
