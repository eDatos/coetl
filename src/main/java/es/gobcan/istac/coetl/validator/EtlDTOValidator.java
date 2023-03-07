package es.gobcan.istac.coetl.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import es.gobcan.istac.coetl.domain.Etl.Type;
import es.gobcan.istac.coetl.domain.enumeration.TipoPlataformaEjecucion;
import es.gobcan.istac.coetl.web.rest.dto.EtlDTO;

public class EtlDTOValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EtlDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "uriRepository", "uriRepository.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "executionPlatform", "executionPlatform.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "type.empty");
        EtlDTO etl = (EtlDTO) target;
        if (TipoPlataformaEjecucion.APACHE_HOP.equals(etl.getExecutionPlatform()) && !hopType(etl.getType()) || 
            TipoPlataformaEjecucion.PENTAHO.equals(etl.getExecutionPlatform()) && !pentahoType(etl.getType())) {
            errors.reject("type.not_supported", "Type value is not supported for execution platform");
        }
    }

    private boolean hopType(Type type) {
        return Type.WORKFLOW.equals(type) || Type.PIPELINE.equals(type);
    }
    
    private boolean pentahoType(Type type) {
        return Type.JOB.equals(type) || Type.TRANSFORMATION.equals(type);
    }
}
