package es.gobcan.istac.coetl.service.validator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.coetl.domain.ComputationalThreads;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Etl.Type;
import es.gobcan.istac.coetl.domain.enumeration.TipoPlataformaEjecucion;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.errors.util.CustomExceptionUtil;
import es.gobcan.istac.coetl.repository.ComputationalThreadsRepository;
import es.gobcan.istac.coetl.repository.EtlRepository;

@Component
public class EtlValidator extends AbstractValidator<Etl> {

    public static final String ETL_HAS_THREADS_CONFIGURED_ERROR = "ETL \"%s\" cannot be deleted as it has been set in the following computational threads: %s";
    
    @Autowired
    private EtlRepository etlRepository;

    @Autowired
    private ComputationalThreadsRepository computationalThreadsRepository;

    @Override
    public void validate(Etl entity) {
        checkCodeIsUnique(entity);
        checkUriRepository(entity);
        checkTypeInPlatform(entity);
        checkStatisticalOperationNotNull(entity);
        checkIfCanDeleteEtl(entity);
    }
    
    private void checkStatisticalOperationNotNull(Etl entity) {
        if (entity.getExternalItem() == null) {
            CustomExceptionUtil.throwCustomParameterizedException(String.format("Statistical Operation is blank in Etl %s", entity.getCode()), ErrorConstants.ETL_STATISTICAL_OPERATION_IS_BLANK);
        }
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

    private void checkUriRepository(Etl entity) {
        if (entity.getUriRepository() == null) { 
            CustomExceptionUtil.throwCustomParameterizedException(String.format("URL repository not found in Etl %s", entity.getCode()), ErrorConstants.ETL_URL_NOT_EXIST);
        }
        
        try {
            new URL(entity.getUriRepository());
        } catch (MalformedURLException e) {
            CustomExceptionUtil.throwCustomParameterizedException(String.format("URL repository '%s' is malformed", entity.getCode()), ErrorConstants.ETL_MALFORMED_URL);
        }
    }

    private void checkTypeInPlatform(Etl entity) {
        if (TipoPlataformaEjecucion.APACHE_HOP.equals(entity.getExecutionPlatform()) && !hopType(entity.getType()) || 
                TipoPlataformaEjecucion.PENTAHO.equals(entity.getExecutionPlatform()) && !pentahoType(entity.getType())) {
            
            CustomExceptionUtil.throwCustomParameterizedException(String.format("Type %s is not supported for execution platform %s", entity.getType().name(), entity.getExecutionPlatform().name()), ErrorConstants.ETL_TYPE_NOT_SUPPORTED);
        }
    }

    private boolean hopType(Type type) {
        return Type.WORKFLOW.equals(type) || Type.PIPELINE.equals(type);
    }

    private boolean pentahoType(Type type) {
        return Type.JOB.equals(type) || Type.TRANSFORMATION.equals(type);
    }

    private void checkIfCanDeleteEtl(Etl entity) {
        if (entity.getId() != null && entity.getDeletionDate() != null) {
            List<ComputationalThreads> threads = computationalThreadsRepository.findAllByComputationalThreadsEtlEtlId(entity.getId());
            if (!threads.isEmpty()) {
                List<String> threadsName = threads.stream().map(thread -> thread.getName()).collect(Collectors.toList());
                CustomExceptionUtil.throwCustomParameterizedException(String.format(ETL_HAS_THREADS_CONFIGURED_ERROR, entity.getName(), threadsName),
                        ErrorConstants.ETL_HAS_THREADS_CONFIGURED, entity.getName(), getErrorMessagge(threadsName));
            }
        }
    }

    private String getErrorMessagge(List<String> threadsName) {
        StringBuilder queryBuilder = new StringBuilder();
        threadsName.forEach(name -> queryBuilder.append("<li>").append(name).append("</li>"));
        return queryBuilder.toString();
    }

}
