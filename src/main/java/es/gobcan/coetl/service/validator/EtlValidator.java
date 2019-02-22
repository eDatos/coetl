package es.gobcan.coetl.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.errors.ErrorConstants;
import es.gobcan.coetl.errors.util.CustomExceptionUtil;
import es.gobcan.coetl.repository.EtlRepository;

@Component
public class EtlValidator extends AbstractValidator<Etl> {

    @Autowired
    private EtlRepository etlRepository;

    @Override
    public void validate(Etl entity) {
        checkCodeIsUnique(entity);
    }

    private void checkCodeIsUnique(Etl entity) {
        if (entity.getId() != null) {
            return;
        }

        Etl foundEtl = etlRepository.findOneByCode(entity.getCode());
        if (foundEtl != null) {
            CustomExceptionUtil.throwCustomParameterizedException(String.format("Etl code %s exists", entity.getCode()), ErrorConstants.ETL_CODE_EXISTS);
        }
    }

}
