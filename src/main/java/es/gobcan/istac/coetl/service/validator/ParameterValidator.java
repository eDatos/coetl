package es.gobcan.istac.coetl.service.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Parameter;
import es.gobcan.istac.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.repository.EtlRepository;
import es.gobcan.istac.coetl.repository.ParameterRepository;

@Component
public class ParameterValidator extends AbstractValidator<Parameter> {

    private static final String FIELD_BLANK_ERROR_MESSAGE = "Field \"%s\" of Parameter (id=%s) can not be blank";
    private static final String FIELD_NULL_ERROR_MESSAGE = "Field \"%s\" of Parameter (id=%s) can not be null";
    private static final String FIELD_DUPLICATED_ERROR_MESSAGE = "Field \"%s\" of Parameter (id=%s) is duplicated";

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private EtlRepository etlRepository;

    @Override
    public void validate(Parameter entity) {
        checkKeyIsNotBlank(entity);
        checkValueIsNotBlank(entity);
        checkTypeIsNotNull(entity);
        checkTypologyIsNotNull(entity);
        checkEtlExists(entity);
        checkKeyIsNotDuplicated(entity);
    }

    private void checkKeyIsNotBlank(Parameter entity) {
        if (StringUtils.isBlank(entity.getKey())) {
            throw new CustomParameterizedExceptionBuilder().message(String.format(FIELD_BLANK_ERROR_MESSAGE, "key", entity.getId())).code(ErrorConstants.PARAMETER_KEY_IS_BLANK).build();
        }
    }

    private void checkValueIsNotBlank(Parameter entity) {
        if (StringUtils.isBlank(entity.getValue())) {
            throw new CustomParameterizedExceptionBuilder().message(String.format(FIELD_BLANK_ERROR_MESSAGE, "value", entity.getId())).code(ErrorConstants.PARAMETER_VALUE_IS_BLANK).build();
        }
    }

    private void checkTypeIsNotNull(Parameter entity) {
        if (entity.getType() == null) {
            throw new CustomParameterizedExceptionBuilder().message(String.format(FIELD_NULL_ERROR_MESSAGE, "type", entity.getId())).code(ErrorConstants.PARAMETER_EDIT).build();
        }
    }

    private void checkTypologyIsNotNull(Parameter entity) {
        if (entity.getTypology() == null) {
            throw new CustomParameterizedExceptionBuilder().message(String.format(FIELD_NULL_ERROR_MESSAGE, "typology", entity.getId())).code(ErrorConstants.PARAMETER_EDIT).build();
        }
    }


    private void checkEtlExists(Parameter entity) {
        Etl etl = etlRepository.findByIdAndDeletionDateIsNull(entity.getEtl().getId());
        if (etl == null) {
            throw new CustomParameterizedExceptionBuilder().message(String.format("Etl (code=%s) of Parameter (id=%s) must exists.", entity.getEtl().getCode(), entity.getId()))
                    .code(ErrorConstants.PARAMETER_EDIT).build();
        }
    }

    private void checkKeyIsNotDuplicated(Parameter entity) {
        //@formatter:off
        Long currentEtlId = entity.getEtl().getId();
        String currentKey = entity.getKey();
        Parameter duplicatedParameterKey = getOriginalEntity(status -> entity.getId() == null
                ? parameterRepository.findByKeyAndEtlId(currentKey, currentEtlId)
                : parameterRepository.findByKeyAndEtlIdAndIdNot(currentKey, currentEtlId, entity.getId()));
       //@formatter:on

        if (duplicatedParameterKey != null) {
            throw new CustomParameterizedExceptionBuilder().message(String.format(FIELD_DUPLICATED_ERROR_MESSAGE, "key", entity.getId())).code(ErrorConstants.PARAMETER_KEY_IS_DUPLICATED).build();
        }
    }
}
