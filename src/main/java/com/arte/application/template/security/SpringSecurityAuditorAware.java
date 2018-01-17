package com.arte.application.template.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.arte.application.template.config.Constants;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentUserLogin();
        return userName != null ? userName : Constants.SYSTEM_ACCOUNT;
    }
}
