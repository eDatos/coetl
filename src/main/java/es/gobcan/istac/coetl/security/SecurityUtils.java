package es.gobcan.istac.coetl.security;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import es.gobcan.istac.coetl.domain.enumeration.Rol;

public final class SecurityUtils {

    private static final String ACL_APP_NAME = "GESTOR_CONSOLA_ETL";
    private static final String SEPARATOR = "#";

    private SecurityUtils() {
    }

    public static String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String userName = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                userName = (String) authentication.getPrincipal();
            }
        }
        return userName;
    }

    public static boolean isAdmin(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext.getAuthentication().getAuthorities().stream().anyMatch(authority -> {
            String[] appRole = authority.getAuthority().split(SEPARATOR);
            String application = appRole[0];
            String roleName = appRole[1];

            return application.equals(ACL_APP_NAME) && Rol.ADMINISTRADOR.name().equalsIgnoreCase(roleName);
        });
    }

    public static boolean haveAccessToOperationInRol(String codeOperation) {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        return securityContext.getAuthentication().getAuthorities().stream().anyMatch(authority -> {
            String[] appRole = authority.getAuthority().split(SEPARATOR);
            String application = appRole[0];
            String roleName = appRole[1];

            if (appRole.length == 3) {
                String finalOperation = appRole[2].length() == 0? null: appRole[2];

                return application.equals(ACL_APP_NAME)
                    && Arrays.stream(Rol.values()).anyMatch(role -> Objects.equals(role.name(), roleName))
                    && finalOperation.equalsIgnoreCase(codeOperation);
            }
            return false;
        });
    }
}
