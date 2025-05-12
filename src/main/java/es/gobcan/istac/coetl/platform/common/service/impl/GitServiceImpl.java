package es.gobcan.istac.coetl.platform.common.service.impl;

import static es.gobcan.istac.coetl.platform.common.util.RemoteConnectionUtils.SftpException;
import static es.gobcan.istac.coetl.platform.common.util.RemoteConnectionUtils.executeCommand;
import static es.gobcan.istac.coetl.platform.common.util.RemoteConnectionUtils.move;
import static es.gobcan.istac.coetl.platform.common.util.RemoteConnectionUtils.remove;
import static es.gobcan.istac.coetl.platform.common.util.RemoteConnectionUtils.mkdirp;
import static es.gobcan.istac.coetl.platform.common.util.RemoteConnectionUtils.listFolder;
import static es.gobcan.istac.coetl.platform.common.util.RemoteConnectionUtils.getSudoDestinationOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.OverthereFile;
import com.xebialabs.overthere.util.CapturingOverthereExecutionOutputHandler;

import es.gobcan.istac.coetl.config.GitProperties;
import es.gobcan.istac.coetl.config.common.PlatformHost;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Parameter;
import es.gobcan.istac.coetl.domain.Parameter.Typology;
import es.gobcan.istac.coetl.domain.enumeration.TipoPlataformaEjecucion;
import es.gobcan.istac.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.platform.common.PlatformPropertiesComponent;
import es.gobcan.istac.coetl.platform.common.service.GitService;
import es.gobcan.istac.coetl.repository.FileRepository;
import es.gobcan.istac.coetl.repository.ParameterRepository;

@Service
public class GitServiceImpl implements GitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitServiceImpl.class);
    
    private static final String REPOSITORY_FOLDER_NAME = "repository";
    private static final String REPOSITORY_FOLDER_BACKUP_NAME = "repositoryBackup";
    private static final String METADATA_FOLDER_NAME = "metadata"; // Usado en repositorios de ETLs para Hop
    private static final String ERROR_CREDENCIALES_GIT = "An error ocurred encoding git credentials";
    private static final String ERROR_URI_INCORRECT = "An error ocurred with URI repository in ETL with code \"%s\"";
    private static final String ERROR_PULL = "An error ocurred executing shell commands while pulling repository";
    private static final String ERROR_FILE_PARAMETER = "An error ocurred while writing parameter files in directory";
    private static final String ERROR_FILE_DESCONOCIDO = "An error ocurred while trying to move the file";
    private static final String ERROR_DESCONOCIDO = "Unknown error ocurred while pulling repository \"%s\"";
    
    @Autowired
    private PlatformPropertiesComponent platformProperties;
    
    @Autowired
    private GitProperties gitProperties;
    
    @Autowired 
    private ParameterRepository parameterRepository;
    
    @Autowired 
    private FileRepository fileRepository;
    
    @Override
    public String cloneRepository(Etl etl) {
        OverthereConnection sudoDestinationConnection = Overthere.getConnection("ssh", getSudoDestinationOptions(platformProperties.determinePropertiesClass(etl).getHost()));
        String path = platformProperties.determinePropertiesClass(etl).getHost().getResourcesPath().concat("/").concat(etl.getCode());
        
        try {
            mkdirp(sudoDestinationConnection, path);
            executeCommand(sudoDestinationConnection, "git", "-C", path, "clone", "--branch", gitProperties.getBranch(), getUrlRepositoryWithCredentials(etl.getUriRepository()));
            move(sudoDestinationConnection, path.concat("/").concat(getFolderRepositoryName(etl)), path.concat("/").concat(REPOSITORY_FOLDER_NAME));
            executeCommand(sudoDestinationConnection, "git", "-C", path.concat("/").concat(REPOSITORY_FOLDER_NAME), "remote", "set-url", "origin", etl.getUriRepository());
            changeOwnerUnzippedFiles(sudoDestinationConnection, path, etl);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("An error ocurred encoding git credentials", e);
            return null;
        } catch (MalformedURLException e) {
            LOGGER.error("An error ocurred with URI repository in ETL with code " + etl.getCode(), e);
            return null;
        } catch (SftpException e) {
            LOGGER.error("An error ocurred executing shell commands while cloning repository", e);
            return null;
        } catch (Exception e) {
            LOGGER.error("Unknown error ocurred while clone repository " + etl.getUriRepository(), e);
            return null;            
        } finally {
            sudoDestinationConnection.close();
        }

        return path.concat("/").concat(REPOSITORY_FOLDER_NAME);
    }

    @Override
    public void updateRepository(Etl etl) {
        
        String path = platformProperties.determinePropertiesClass(etl).getHost().getResourcesPath().concat("/").concat(etl.getCode()).concat("/").concat(REPOSITORY_FOLDER_NAME);
        try (OverthereConnection sudoDestinationConnection = Overthere.getConnection("ssh", getSudoDestinationOptions(platformProperties.determinePropertiesClass(etl).getHost()));){
            executeCommand(sudoDestinationConnection, "git", "-C", path, "remote", "set-url", "origin", getUrlRepositoryWithCredentials(etl.getUriRepository()));
            executeCommand(sudoDestinationConnection, "git", "-C", path, "pull");
            executeCommand(sudoDestinationConnection, "git", "-C", path, "remote", "set-url", "origin", etl.getUriRepository());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(ERROR_CREDENCIALES_GIT);
            throw new CustomParameterizedExceptionBuilder().message(ERROR_CREDENCIALES_GIT).code(ErrorConstants.EXECUTION_CREDENTIALS_ERROR).build();
        } catch (MalformedURLException e) {
            LOGGER.error(String.format(ERROR_URI_INCORRECT, etl.getCode()), e);
            throw new CustomParameterizedExceptionBuilder().message(String.format(ERROR_URI_INCORRECT, etl.getCode()))
            .code(ErrorConstants.EXECUTION_URI_ERROR).build();
        }catch (SftpException e) {
            LOGGER.error(ERROR_PULL, e);
            throw new CustomParameterizedExceptionBuilder().message(ERROR_PULL).code(ErrorConstants.EXECUTION_PULL_ERROR).build();
        } catch (Exception e) {
            LOGGER.error(String.format(ERROR_DESCONOCIDO, etl.getUriRepository()), e);
            throw new CustomParameterizedExceptionBuilder().message(String.format(ERROR_DESCONOCIDO, etl.getUriRepository()))
            .code(ErrorConstants.EXECUTION_UNKNOWN_ERROR).build();
        }
    }
    
    @Override
    public String replaceRepository(Etl etl, String oldPath) {
        OverthereConnection sudoDestinationConnection = Overthere.getConnection("ssh", getSudoDestinationOptions(platformProperties.determinePropertiesClass(etl).getHost()));
        String path = oldPath.replace(REPOSITORY_FOLDER_NAME, "");
        String newRepository = null;
        
        try {
            move(sudoDestinationConnection, path.concat(REPOSITORY_FOLDER_NAME), path.concat(REPOSITORY_FOLDER_BACKUP_NAME));
            
            newRepository = cloneRepository(etl);
            if (newRepository == null) {
                remove(sudoDestinationConnection, path.concat(REPOSITORY_FOLDER_NAME));
                move(sudoDestinationConnection, path.concat(REPOSITORY_FOLDER_BACKUP_NAME), path.concat(REPOSITORY_FOLDER_NAME));
            } else {
                checkFileParameters(etl, path.concat(REPOSITORY_FOLDER_BACKUP_NAME), newRepository, sudoDestinationConnection);
                remove(sudoDestinationConnection, path.concat(REPOSITORY_FOLDER_BACKUP_NAME));
            }
        } catch (Exception e) {
            LOGGER.error("Unknown error ocurred while replacing repository " + etl.getUriRepository(), e);
            return null;
        } finally {
            sudoDestinationConnection.close();
        }
        
        return newRepository;
    }
    
    @Override
    public void deleteRepository(Etl etl) {
        deleteRepository(etl.getCode(), etl.getExecutionPlatform());
    }
    
    @Override
    public void deleteRepository(String code, TipoPlataformaEjecucion platform) {
        OverthereConnection sudoDestinationConnection = Overthere.getConnection("ssh", getSudoDestinationOptions(platformProperties.determinePropertiesClass(platform).getHost()));
        String path = platformProperties.determinePropertiesClass(platform).getHost().getResourcesPath().concat("/").concat(code);
        try {
            remove(sudoDestinationConnection, path);
        } catch (Exception e) {
            LOGGER.error(String.format(ERROR_DESCONOCIDO, code), e);
            throw new CustomParameterizedExceptionBuilder().message(String.format(ERROR_DESCONOCIDO, code))
            .code(ErrorConstants.EXECUTION_UNKNOWN_ERROR).build();
        } finally {
            sudoDestinationConnection.close();
        }
    }
    
    @Override
    public String getMainFileContent(Etl etl) throws UnsupportedEncodingException {
        String result = null;
        String basePath = platformProperties.determinePropertiesClass(etl).getHost().getResourcesPath().concat("/").concat(etl.getCode()).concat("/" + REPOSITORY_FOLDER_NAME + "/");
        try (OverthereConnection sudoSourceConnection = Overthere.getConnection("ssh", getSudoDestinationOptions(platformProperties.determinePropertiesClass(etl).getHost()));) {
            CapturingOverthereExecutionOutputHandler oh = CapturingOverthereExecutionOutputHandler.capturingHandler();
            executeCommand(sudoSourceConnection, oh, "ls", basePath.concat(platformProperties.determinePropertiesClass(etl).getMainResourcePrefix() + "*"));
            String mainFileNamePath = oh.getOutputLines().get(oh.getOutputLines().size()-1).trim();
            
            OverthereFile sourceMainFile = sudoSourceConnection.getFile(mainFileNamePath);
            result = new BufferedReader(new InputStreamReader(sourceMainFile.getInputStream(), StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            LOGGER.error(String.format(ERROR_DESCONOCIDO, etl.getCode()), e);
            throw new CustomParameterizedExceptionBuilder().message(String.format(ERROR_DESCONOCIDO, etl.getCode()))
            .code(ErrorConstants.EXECUTION_UNKNOWN_ERROR).build();
        }

        return result;
    }
    
    @Override
    public String getMainFileName(Etl etl) {

        CapturingOverthereExecutionOutputHandler oh = CapturingOverthereExecutionOutputHandler.capturingHandler();
        String basePath = platformProperties.determinePropertiesClass(etl).getHost().getResourcesPath().concat("/").concat(etl.getCode()).concat("/" + REPOSITORY_FOLDER_NAME + "/");
        
        try (OverthereConnection sudoSourceConnection = Overthere.getConnection("ssh", getSudoDestinationOptions(platformProperties.determinePropertiesClass(etl).getHost()));) {
            executeCommand(sudoSourceConnection, oh, "ls", basePath.concat(platformProperties.determinePropertiesClass(etl).getMainResourcePrefix() + "*"));
        } catch (Exception e) {
            LOGGER.error(String.format(ERROR_DESCONOCIDO, etl.getCode()), e);
            throw new CustomParameterizedExceptionBuilder().message(String.format(ERROR_DESCONOCIDO, etl.getCode()))
            .code(ErrorConstants.EXECUTION_UNKNOWN_ERROR).build();
        }
        String mainFileNamePath = oh.getOutputLines().get(oh.getOutputLines().size()-1).trim();
        
        return mainFileNamePath.substring(mainFileNamePath.lastIndexOf('/') + 1).split("\\.")[0];
    }
    
    @Override
    public Map<String, List<String>> getEtlMetadataInfo(Etl etl) throws UnsupportedEncodingException {
        PlatformHost platformHost= platformProperties.determinePropertiesClass(etl).getHost();
        String basePath = platformProperties.determinePropertiesClass(etl).getHost().getResourcesPath().concat("/").concat(etl.getCode()).concat("/" + REPOSITORY_FOLDER_NAME + "/");
        
        Map<String, List<String>> result = new HashMap<>();
        try (OverthereConnection sudoSourceConnection = Overthere.getConnection("ssh", getSudoDestinationOptions(platformHost));) {
            List<OverthereFile> list = listFolder(sudoSourceConnection, basePath.concat(METADATA_FOLDER_NAME));
            
            int initLoop = (platformHost.getUsername().equals(platformHost.getSudoUsername())) ? 0 : 1;
            for (int i = initLoop; i < list.size(); i++) {
                OverthereFile metadataSubFolder = list.get(i);
                result.put(metadataSubFolder.getName(), getFilesContentFromFolder(etl, metadataSubFolder.getPath(), initLoop));
            }
        } catch (Exception e) {
            LOGGER.error("Metadata folder don't exists", e);
            return result;
        }
        
        return result;
    }
    
    @Override
    public void checkFileParameters(Etl etl, String originalPath, String newPath, OverthereConnection sudoDestinationConnection) {
        List<Parameter> listaParametro = parameterRepository.findAllByEtlIdAndTypology(etl.getId(), Typology.FILE);
        for (Parameter param : listaParametro) {
            es.gobcan.istac.coetl.domain.File file = fileRepository.findOneById(param.getFile());
            String originalFilePath = originalPath.concat("/").concat(file.getName());
            String newFilePath = newPath.concat("/").concat(file.getName());
            try {
                File f = new File(newFilePath);
                if (!f.exists()) {
                    move(sudoDestinationConnection, originalFilePath, newFilePath);
                    changeOwnerUnzippedFiles(sudoDestinationConnection, newFilePath, etl);
                } else {
                    throw new CustomParameterizedExceptionBuilder().message(ERROR_FILE_PARAMETER)
                            .code(ErrorConstants.PARAMETER_FILE_ALREADY_EXISTS).build();
                }
            } catch (Exception e) {
                LOGGER.error("Error ocurred while moving file. {}", file.getName(), e);
                throw new CustomParameterizedExceptionBuilder().message(ERROR_FILE_DESCONOCIDO)
                .code(ErrorConstants.ERROR_FILE_DESCONOCIDO).build();
            }
        }
    }
    
    @Override
    public void checkFileParameters(Etl etl, String originalPath, String newPath) {
        OverthereConnection connection = Overthere.getConnection("ssh", getSudoDestinationOptions(platformProperties.determinePropertiesClass(etl).getHost()));
        checkFileParameters(etl, originalPath, newPath, connection);
    }
    
    private List<String> getFilesContentFromFolder(Etl etl, String folder, int initLoop) {
        List<String> result = new ArrayList<>();
        
        try (OverthereConnection sudoSourceConnection = Overthere.getConnection("ssh", getSudoDestinationOptions(platformProperties.determinePropertiesClass(etl).getHost()));) {

            List<OverthereFile> list = listFolder(sudoSourceConnection, folder);
            for (int i = initLoop; i < list.size(); i++) {
                OverthereFile metadataInfo = sudoSourceConnection.getFile(list.get(i).getPath());
                String content = new BufferedReader(new InputStreamReader(metadataInfo.getInputStream(), StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
                result.add(content);
            }
        }

        return result;
    }
    
    private String getUrlRepositoryWithCredentials(String urlRepository) throws UnsupportedEncodingException, MalformedURLException {
        URL url = new URL(urlRepository);
        return url.getProtocol()
                .concat("://")
                .concat(URLEncoder.encode(gitProperties.getUsername(), "UTF-8"))
                .concat(":")
                .concat(URLEncoder.encode(gitProperties.getPassword(), "UTF-8"))
                .concat("@")
                .concat(url.getAuthority()).concat(url.getPath());
    }
    
    private String getFolderRepositoryName(Etl etl) {
        String folder = etl.getUriRepository().substring(etl.getUriRepository().lastIndexOf('/') + 1);
        folder = folder.replace(".git", "");
        return folder;
    }
    
    private void changeOwnerUnzippedFiles(OverthereConnection sudoConnection, String path, Etl etl) {
        String chownParameter = platformProperties.determinePropertiesClass(etl).getHost().getOwnerUserResourcesPath().concat(":").concat(platformProperties.determinePropertiesClass(etl).getHost().getOwnerGroupResourcesPath());
        executeCommand(sudoConnection, "chown", chownParameter, "-R", path);
    }
       
}
