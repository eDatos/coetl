package es.gobcan.istac.coetl.service.impl;

import es.gobcan.istac.coetl.config.Constants;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.File;
import es.gobcan.istac.coetl.domain.Parameter;
import es.gobcan.istac.coetl.domain.Parameter.Type;
import es.gobcan.istac.coetl.domain.Parameter.Typology;
import es.gobcan.istac.coetl.repository.ParameterRepository;
import es.gobcan.istac.coetl.security.SecurityUtils;
import es.gobcan.istac.coetl.service.FileService;
import es.gobcan.istac.coetl.service.GlobalParameterService;
import es.gobcan.istac.coetl.service.ParameterService;
import es.gobcan.istac.coetl.service.validator.ParameterValidator;
import es.gobcan.istac.coetl.web.rest.mapper.ParameterMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ParameterServiceImpl implements ParameterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterService.class);

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private ParameterValidator parameterValidator;

    @Autowired
    private GlobalParameterService globalParameterService;
    
    @Autowired 
    private ParameterMapper parameterMapper;
    
    @Autowired
    private FileService fileService;


    @Override
    public List<Parameter> createDefaultParameters(Etl etl, Map<String, String> parameters) {
        LOGGER.debug("Request to create default Parameters : {} of ETL : {}", parameters, etl);
        //@formatter:off
        return parameters.entrySet().stream()
                .map(entry -> {
                    Parameter parameter = new Parameter();
                    parameter.setEtl(etl);
                    parameter.setKey(entry.getKey());
                    parameter.setValue(entry.getValue());
                    parameter.setType(Type.AUTO);
                    parameter.setTypology(Typology.GENERIC);
                    return parameter;
                })
                .map(this::save)
                .collect(Collectors.toList());
        //@formatter:on
    }

    @Override
    public void deleteDefaultParameters(Etl etl) {
        LOGGER.debug("Request to delete default Parameters of ETL : {}", etl);
        List<Parameter> defaultParameters = parameterRepository.findAllByEtlIdAndType(etl.getId(), Type.AUTO);
        defaultParameters.forEach(this::delete);
        parameterRepository.flush();
    }

    @Override
    public Parameter create(Parameter parameter) {
        LOGGER.debug("Request to create a Parameter : {}", parameter);
        parameterValidator.validate(parameter);
        return save(parameter);
    }

    @Override
    public Parameter update(Parameter parameter) {
        LOGGER.debug("Request to update a Parameter : {}", parameter);
        parameterValidator.validate(parameter);
        return save(parameter);
    }

    @Override
    public void delete(Parameter parameter) {
        LOGGER.debug("Request to delete a Parameter : {}", parameter);
        parameterRepository.delete(parameter);
    }

    @Override
    public List<Parameter> findAllByEtlId(Long etlId) {
        LOGGER.debug("Request to find all Parameters by ETL: {}", etlId);
        return parameterRepository.findAllByEtlId(etlId);
    }

    @Override
    public Map<String, String> findAllByEtlIdAsMap(Long etlId) {
        List<Parameter> parameters = parameterRepository.findAllByEtlId(etlId);
        Map<String, String> etlParameters = parameters.stream().collect(Collectors.toMap(Parameter::getKey, p -> decodeValueByTypology(p)));
        etlParameters.putAll(globalParameterService.findAllGlobalParametersAsMap());
        return etlParameters;
    }

    @Override
    public String decodeValueByTypology(Parameter parameter){
        if(Typology.PASSWORD.equals(parameter.getTypology())){
            return SecurityUtils.passwordDecode(parameter.getValue());
        }
        return parameter.getValue();
    }

    @Override
    public Parameter findOneByIdAndEtlId(Long id, Long etlId) {
        LOGGER.debug("Request to get a Parameter by id: {} and etlId: {}", id, etlId);
        parameterRepository.flush();
        return parameterRepository.findByIdAndEtlId(id, etlId);
    }
    
    @Override
    public Parameter findOneByKeyAndEtlId(String key, Long etlId) {
        LOGGER.debug("Request to get one Parameter by key: {} and etlId: {}", key, etlId);
        return parameterRepository.findOneByKeyAndEtlId(key, etlId);
    }

    private Parameter save(Parameter parameter) {
        LOGGER.debug("Request to save a Parameter : {}", parameter);
        return parameterRepository.saveAndFlush(parameter);
    }

    @Override
    public Parameter findOneById(Long id) {
        LOGGER.debug("Request to get a Parameter by id : {}", id);
        return parameterRepository.findOneById(id);
    }

    @Override
    public Parameter getOneById(Long paramId) {
        LOGGER.debug("Request to find a single Parameter");
        return parameterRepository.getOne(paramId);
    }
    
    public Parameter copyParameter(Parameter originalParameter) {
        Parameter newParam = new Parameter();
        //newParam.setDescription(originalParameter.getDescription());
        newParam.setEtl(originalParameter.getEtl());
        newParam.setFile(originalParameter.getFile());
        newParam.setId(originalParameter.getId());
        newParam.setKey(originalParameter.getKey());
        newParam.setOptLock(originalParameter.getOptLock());
        newParam.setType(originalParameter.getType());
        newParam.setTypology(originalParameter.getTypology());
        newParam.setValue(originalParameter.getValue());

        return newParam;
    }

    @Override
    public Long storeFile(MultipartFile file, Long idEtl) {
        parameterValidator.checkIfFileAlreadyExists(file.getOriginalFilename(), idEtl);
        Parameter parameter = findOneByKeyAndEtlId(Constants.ETL_RESOURCES, idEtl);
        if (parameter != null) {
            String etlResourcesPath = parameterMapper.toDto(parameter).getValue();
            File savedFile = fileService.saveDatabase(file);
            Path dir = Paths.get(etlResourcesPath);
            fileService.uploadRepository(dir, file);
            return savedFile.getId();
        }
        return null;
    }

    private void changeTypologyFromFileToOther(Parameter currentParameter, Long fileIdNum, Path repositoryPath, String originalFilename) {
        currentParameter.setFile(null);
        fileService.deleteDatabase(fileIdNum);
        fileService.deleteRepository(repositoryPath, originalFilename);
    }

    private void changeTypologyToFile(Parameter currentParameter, Path repositoryPath, MultipartFile file, Long idEtl) {
        // If filename changes, check if there is no other file with that name.
        parameterValidator.checkIfFileAlreadyExists(file.getOriginalFilename(), idEtl);
        File fichero = fileService.saveDatabase(file);
        Long fileId = fichero.getId();
        currentParameter.setFile(fileId);
        fileService.uploadRepository(repositoryPath, file);
    }

    private void sameTypologyIsFile(Long fileIdNum, Path repositoryPath, MultipartFile file, String originalFilename, Long idEtl) {
        if (!originalFilename.equals(file.getOriginalFilename())) {
            // If filename changes, check if there is no other file with that name.
            parameterValidator.checkIfFileAlreadyExists(file.getOriginalFilename(), idEtl);
        }
        fileService.updateRepository(repositoryPath, file, originalFilename);
        fileService.updateDatabase(file, fileIdNum);
    }
    
    private void differentTypologies(Typology originalTypology, Typology newTypology, Parameter currentParameter, Long fileIdNum, Path repositoryPath, MultipartFile file, String originalFilename, Long idEtl) {
        if(originalTypology == Typology.FILE) {
            // Change from "FILE" to other typology -> Update param and delete file from database and repository
            changeTypologyFromFileToOther(currentParameter, fileIdNum, repositoryPath, originalFilename);
        }
        if(newTypology == Typology.FILE) {
            changeTypologyToFile(currentParameter, repositoryPath, file, idEtl);
        }
    }

    
    private void checkTypologies(Typology originalTypology, Typology newTypology, Parameter currentParameter, Long fileIdNum, Path repositoryPath, MultipartFile file, String originalFilename, Long idEtl){
        if(originalTypology != newTypology) {
            differentTypologies(originalTypology, newTypology, currentParameter, fileIdNum, repositoryPath, file, originalFilename, idEtl);
        } else {
            if(originalTypology == Typology.FILE) {
                // Update an existing file
                sameTypologyIsFile(fileIdNum, repositoryPath, file, originalFilename, idEtl);   
            }
        }
    }


    @Override
    public void updateFile(MultipartFile file, Parameter originalParameter, Parameter currentParameter, Long idEtl) {    
        String originalFilename = originalParameter.getValue();
        Typology originalTypology = originalParameter.getTypology();
        Parameter etlPathParameter = findOneByKeyAndEtlId(Constants.ETL_RESOURCES, idEtl);
        Long fileIdNum = originalParameter.getFile();
        Typology newTypology = currentParameter.getTypology();
        Path repositoryPath = etlPathParameter.getValue() == null ? null : Paths.get(etlPathParameter.getValue());
        checkTypologies(originalTypology, newTypology, currentParameter, fileIdNum, repositoryPath, file, originalFilename, idEtl);
    }



    @Override
    public void deleteFile(Parameter parameter, Long idEtl) {
        // Delete file from repository folder and database
        fileService.deleteDatabase(parameter.getFile());
        Parameter etlPathParam = findOneByKeyAndEtlId(Constants.ETL_RESOURCES, idEtl);
        Path dir = Paths.get(etlPathParam.getValue());
        fileService.deleteRepository(dir, parameter.getValue());
        
    }

}
