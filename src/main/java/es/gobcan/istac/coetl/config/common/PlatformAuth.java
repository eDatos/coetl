package es.gobcan.istac.coetl.config.common;

import org.apache.commons.lang3.StringUtils;

public class PlatformAuth {
    
    private String user = StringUtils.EMPTY;
    private String password = StringUtils.EMPTY;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
