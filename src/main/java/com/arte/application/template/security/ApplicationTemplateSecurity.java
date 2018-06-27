package com.arte.application.template.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("secChecker")
public class ApplicationTemplateSecurity {

    public boolean checkAuditoriaPermission(Authentication auth) {
        return true;
    }
    
    public boolean checkLogsPermission(Authentication auth) {
        return true;
    }
    
    public boolean checkUsuarioPermission(Authentication auth) {
        return true;
    }
}