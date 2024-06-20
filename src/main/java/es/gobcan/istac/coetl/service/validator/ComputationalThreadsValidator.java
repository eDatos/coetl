package es.gobcan.istac.coetl.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.coetl.domain.ComputationalThreads;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.errors.util.CustomExceptionUtil;
import es.gobcan.istac.coetl.repository.ComputationalThreadsRepository;

@Component
public class ComputationalThreadsValidator extends AbstractValidator<ComputationalThreads> {

    @Autowired
    private ComputationalThreadsRepository computationalThreadsRepository;

    @Override
    public void validate(ComputationalThreads entity) {
        checkCodeIsUnique(entity);
        checkStatisticalOperationNotNull(entity);
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

}
