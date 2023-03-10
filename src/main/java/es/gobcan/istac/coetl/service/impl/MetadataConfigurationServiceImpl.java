package es.gobcan.istac.coetl.service.impl;


import org.siemac.edatos.core.common.conf.ConfigurationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.coetl.service.DataConfigurationService;
import es.gobcan.istac.coetl.service.MetadataConfigurationService;


@Service
public class MetadataConfigurationServiceImpl extends ConfigurationServiceImpl implements MetadataConfigurationService {

    @Autowired
    private DataConfigurationService instanceService;

    @Override
    public String retrieveCasService() {
        return retrieveProperty(instanceService.getCasService());
    }

    @Override
    public String retrieveKeyCas() {
        return retrieveProperty(instanceService.getKeyCas());
    }

    @Override
    public String retrieveDbUrl() {
        return retrieveProperty(instanceService.getDbUrl());
    }

    @Override
    public String retrieveDbUsername() {
        return retrieveProperty(instanceService.getDbUsername());
    }

    @Override
    public String retrieveDbPassword() {
        return retrieveProperty(instanceService.getDbPassword());
    }

    @Override
    public String retrieveDdDriverName() {
        return retrieveProperty(instanceService.getDdDriverName());
        
    }

    @Override
    public String retrieveGitUser() {
        return retrieveProperty(instanceService.getMetamacKeyGitUser());
        
    }

    @Override
    public String retrieveGitPassword() {
        return retrieveProperty(instanceService.getMetamacKeyGitPassword());
        
    }

    @Override
    public String retrieveGitBranch() {
        return retrieveProperty(instanceService.getMetamacKeyGitBranch());
        
    }

    @Override
    public String retrievePentahoEndpoint() {
        return retrieveProperty(instanceService.getMetamacKeyPentahoEndpoint());
        
    }

    @Override
    public String retrievePentahoAuthUser() {
        return retrieveProperty(instanceService.getMetamacKeyPentahoAuthUser());
        
    }

    @Override
    public String retrievePentahoAuthPassword() {
        return retrieveProperty(instanceService.getMetamacKeyPentahoAuthPassword());
        
    }

    @Override
    public String retrievePentahoHostOs() {
        return retrieveProperty(instanceService.getMetamacKeyPentahoHostOs());
        
    }

    @Override
    public String retrievePentahoHostAddress() {
        return retrieveProperty(instanceService.getMetamacKeyPentahoHostAddress());
        
    }

    @Override
    public String retrievePentahoHostUsername() {
        return retrieveProperty(instanceService.getMetamacKeyPentahoHostUsername());
        
    }

    @Override
    public String retrievePentahoHostPassword() {
        return retrieveProperty(instanceService.getMetamacKeyPentahoHostPassword());
        
    }

    @Override
    public String retrievePentahoHostSudoUsername() {
        return retrieveProperty(instanceService.getMetamacKeyPentahoHostSudoUsername());
        
    }

    @Override
    public String retrievePentahoHostSudopassword() {
        return retrieveProperty(instanceService.getMetamacKeyPentahoHostSudopassword());
        
    }

    @Override
    public String retrievePentahoHostSudoPasswordProptRegex() {
        return retrieveProperty(instanceService.getMetamacKeyPentahoHostSudoPasswordProptRegex());
        
    }

    @Override
    public String retrievePentahoHostSftpPath() {
        return retrieveProperty(instanceService.getMetamacKeyPentahoHostSftpPath());
        
    }

    @Override
    public String retrievePentahoHostResourcesPath() {
        return retrieveProperty(instanceService.getMetamacKeyPentahoHostResourcesPath());
        
    }

    @Override
    public String retrievePentahoHostOwnerUserResourcesPath() {
        return retrieveProperty(instanceService.getMetamacKeyPentahoHostOwnerUserResourcesPath());
        
    }

    @Override
    public String retrievePentahoHostOwnerGroupResourcesPath() {
        return retrieveProperty(instanceService.getMetamacKeyPentahoHostOwnerGroupResourcesPath());
        
    }

    @Override
    public String retrievePentahoMainResourcePrefix() {
        return retrieveProperty(instanceService.getMetamacKeyPentahoMainResourcePrefix());
        
    }

    @Override
    public String retrieveApacheHopEndpoint() {
        return retrieveProperty(instanceService.getMetamacKeyApacheHopEndpoint());
        
    }

    @Override
    public String retrieveApacheHopAuthUser() {
        return retrieveProperty(instanceService.getMetamacKeyApacheHopAuthUser());
        
    }

    @Override
    public String retrieveApacheHopAuthPassword() {
        return retrieveProperty(instanceService.getMetamacKeyApacheHopAuthPassword());
        
    }

    @Override
    public String retrieveApacheHopHostOs() {
        return retrieveProperty(instanceService.getMetamacKeyApacheHopHostOs());
        
    }

    @Override
    public String retrieveApacheHopHostAddress() {
        return retrieveProperty(instanceService.getMetamacKeyApacheHopHostAddress());
        
    }

    @Override
    public String retrieveApacheHopHostUsername() {
        return retrieveProperty(instanceService.getMetamacKeyApacheHopHostUsername());
        
    }

    @Override
    public String retrieveApacheHopHostPassword() {
        return retrieveProperty(instanceService.getMetamacKeyApacheHopHostPassword());
        
    }

    @Override
    public String retrieveApacheHopHostSudoUsername() {
        return retrieveProperty(instanceService.getMetamacKeyApacheHopHostSudoUsername());
        
    }

    @Override
    public String retrieveApacheHopHostSudopassword() {
        return retrieveProperty(instanceService.getMetamacKeyApacheHopHostSudopassword());
        
    }

    @Override
    public String retrieveApacheHopHostSudoPasswordProptRegex() {
        return retrieveProperty(instanceService.getMetamacKeyApacheHopHostSudoPasswordProptRegex());
        
    }

    @Override
    public String retrieveApacheHopHostSftpPath() {
        return retrieveProperty(instanceService.getMetamacKeyApacheHopHostSftpPath());
        
    }

    @Override
    public String retrieveApacheHopHostResourcesPath() {
        return retrieveProperty(instanceService.getMetamacKeyApacheHopHostResourcesPath());
        
    }

    @Override
    public String retrieveApacheHopHostOwnerUserResourcesPath() {
        return retrieveProperty(instanceService.getMetamacKeyApacheHopHostOwnerUserResourcesPath());
        
    }

    @Override
    public String retrieveApacheHopHostOwnerGroupResourcesPath() {
        return retrieveProperty(instanceService.getMetamacKeyApacheHopHostOwnerGroupResourcesPath());
        
    }

    @Override
    public String retrieveApacheHopMainResourcePrefix() {
        return retrieveProperty(instanceService.getMetamacKeyApacheHopMainResourcePrefix());
        
    }

    @Override
    public String retrieveApacheHopJsonMetadata() {
        return retrieveProperty(instanceService.getMetamacKeyApacheHopJsonMetadata());
        
    }
    

}
