package es.gobcan.istac.coetl.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.coetl.domain.ComputationalThreads;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.errors.util.CustomExceptionUtil;
import es.gobcan.istac.coetl.repository.ComputationalThreadsRepository;

@Component
public class ComputationalThreadsValidator extends AbstractValidator<ComputationalThreads> {

    private static final Integer FIELD_CODE_SIZE = 255;
    private static final Integer FIELD_NAME_SIZE = 255;
    private static final String FIELD_LIMIT_EXCEED_ERROR = "%s field can not exceed %s characters";

    @Autowired
    private ComputationalThreadsRepository computationalThreadsRepository;

    @Override
    public void validate(ComputationalThreads entity) {
        checkCodeIsUnique(entity);
        checkStatisticalOperationNotNull(entity);
        checkCodeLimit(entity.getCode());
        checkNameLimit(entity.getName());
    }

    private void checkCodeIsUnique(ComputationalThreads entity) {
        if (entity.getId() != null) {
            return;
        }

        ComputationalThreads foundThread = computationalThreadsRepository.findOneByCode(entity.getCode());
        if (foundThread != null) {
            CustomExceptionUtil.throwCustomParameterizedException(String.format("Computational Thread code %s exists", entity.getCode()), ErrorConstants.COMPUTATIONAL_THREAD_CODE_EXISTS);
        }
    }

    private void checkStatisticalOperationNotNull(ComputationalThreads entity) {
        if (entity.getExternalItem() == null) {
            CustomExceptionUtil.throwCustomParameterizedException(String.format("Statistical Operation is blank in Computational Thread %s", entity.getCode()), ErrorConstants.COMPUTATIONAL_THREAD_STATISTICAL_OPERATION_IS_BLANK);
        }
    }

    private void checkCodeLimit(String code) {
        if (code != null && Integer.compare(code.length(), FIELD_CODE_SIZE) == 1) {
            CustomExceptionUtil.throwCustomParameterizedException(String.format(FIELD_LIMIT_EXCEED_ERROR, "code", FIELD_CODE_SIZE.toString()),
                    ErrorConstants.COMPUTATIONAL_THREAD_FIELD_LIMIT_EXCEED_ERROR, "code", FIELD_CODE_SIZE.toString());
        }
    }

    private void checkNameLimit(String name) {
        if (name != null && Integer.compare(name.length(), FIELD_NAME_SIZE) == 1) {
            CustomExceptionUtil.throwCustomParameterizedException(String.format(FIELD_LIMIT_EXCEED_ERROR, "name", FIELD_NAME_SIZE.toString()),
                    ErrorConstants.COMPUTATIONAL_THREAD_FIELD_LIMIT_EXCEED_ERROR, "name", FIELD_NAME_SIZE.toString());
        }
    }

}
