package es.gobcan.istac.coetl.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import es.gobcan.istac.coetl.domain.enumeration.Rol;
import es.gobcan.istac.coetl.security.util.AESUtils;

public final class SecurityUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityUtils.class);

    private static final String ACL_APP_NAME = "GESTOR_CONSOLA_ETL";
    private static final String SEPARATOR = "#";

    private SecurityUtils() {
    }

    public static String passwordEncoder(String password){
        String encodePassword = null;
        try {
            encodePassword = AESUtils.encrypt(password);
        } catch (NoSuchPaddingException | InvalidAlgorithmParameterException e) {
            LOG.error("Error encrypt password ", e);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Error encrypt password. Not souch algorithm  ", e);
        }
        return encodePassword;
    }

    public static String passwordDecode(String password) {
        String decodePassword = null;
        try {
            decodePassword = AESUtils.decrypt(password);
        } catch (NoSuchPaddingException | InvalidAlgorithmParameterException  e) {
            LOG.error("Error decrypt password ", e);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Error decrypt password. Not souch algorithm  ", e);
        }
        return decodePassword;
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
