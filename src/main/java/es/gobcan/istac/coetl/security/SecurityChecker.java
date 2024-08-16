package es.gobcan.istac.coetl.security;

import es.gobcan.istac.coetl.domain.ComputationalThreads;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.enumeration.Rol;
import es.gobcan.istac.coetl.service.ComputationalThreadsService;
import es.gobcan.istac.coetl.service.EtlService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component("secChecker")
public class SecurityChecker {
    
    @Autowired
    private EtlService etlService;
    
    @Autowired
    private ComputationalThreadsService computationalThreadsService;

    private boolean hasRole(Authentication authentication, Rol... userRoles) {
        return authentication.getAuthorities().stream().anyMatch(authority -> {
            String[] appRole = authority.getAuthority().split(SecurityUtils.SEPARATOR);
            String application = appRole[0];
            String roleName = appRole[1];
            return application.equals(SecurityUtils.ACL_APP_NAME) && Arrays.stream(userRoles).anyMatch(role -> Objects.equals(role.name(), roleName));
        });
    }

    public boolean puedeConsultarAuditoria(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean puedeConsultarLogs(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean puedeModificarLogs(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean canReadFile(Authentication authentication) {
        return this.isAdmin(authentication) || this.isTecnico(authentication) || this.isLector(authentication);
    }

    public boolean canManageFile(Authentication authentication) {
        return this.isAdmin(authentication) || this.isTecnico(authentication);
    }

    public boolean puedeConsultarMetrica(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean puedeConsultarSalud(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean puedeGestionarSalud(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean puedeConsultarConfig(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean canManageGlobalParameters(Authentication authentication) {
        return this.isAdmin(authentication);
    }
    
    public boolean canReadGlobalParameters(Authentication authentication) {
        return this.isAdmin(authentication) || this.isTecnico(authentication) || this.isLector(authentication);
    }

    public boolean canReadEtl(Authentication authentication) {
        return this.isAdmin(authentication) || this.isTecnico(authentication) || this.isLector(authentication);
    }
    
    public boolean canReadEtl(Authentication authentication, Long idEtl) {
        if (this.isAdmin(authentication)) {
            return true;
        } else if (this.isTecnico(authentication) || this.isLector(authentication)) {
            Etl etl = etlService.findOne(idEtl);
            if (etl != null) {
                if (etl.getExternalItem() == null) {
                    return true;
                } else {
                    return hasStatisticalOperationInAnyRole(authentication, etl.getExternalItem().getCode());
                }
            }
        }
        return false;
    }

    public boolean canReadComputationalThread(Authentication authentication) {
        return this.isAdmin(authentication) || this.isTecnico(authentication) || this.isLector(authentication);
    }
    
    public boolean canReadComputationalThread(Authentication authentication, Long idComputationalThread) {
        if (this.isAdmin(authentication)) {
            return true;
        } else if (this.isTecnico(authentication) || this.isLector(authentication)) {
            ComputationalThreads cthread = computationalThreadsService.findOne(idComputationalThread);
            if (cthread != null) {
                if (cthread.getExternalItem() == null) {
                    return true;
                } else {
                    return hasStatisticalOperationInAnyRole(authentication, cthread.getExternalItem().getCode());
                }
            }
        }
        return false;
    }
    
    public boolean canManageEtl(Authentication authentication) {
        return this.isAdmin(authentication) || this.isTecnico(authentication);
    }

    public boolean canManageEtl(Authentication authentication, Long idEtl) {
        if (this.isAdmin(authentication)) {
            return true;
        } else if (this.isTecnico(authentication)) {
            Etl etl = etlService.findOne(idEtl);
            if (etl != null) {
                if (etl.getExternalItem() == null) {
                    return true;
                } else {
                    return hasStatisticalOperationInRole(authentication, Rol.TECNICO_PRODUCCION, etl.getExternalItem().getCode());
                }
            }
        }
        return false;
    }

    public boolean canManageComputationalThread(Authentication authentication) {
        return this.isAdmin(authentication) || this.isTecnico(authentication);
    }
    
    public boolean canManageComputationalThread(Authentication authentication, Long idComputationalThread) {
        if (this.isAdmin(authentication)) {
            return true;
        } else if (this.isTecnico(authentication)) {
            ComputationalThreads cthread = computationalThreadsService.findOne(idComputationalThread);
            if (cthread != null) {
                if (cthread.getExternalItem() == null) {
                    return true;
                } else {
                    return hasStatisticalOperationInRole(authentication, Rol.TECNICO_PRODUCCION, cthread.getExternalItem().getCode());
                }
            }
        }
        
        return false;
    }

    private boolean isAdmin(Authentication authentication) {
        return this.hasRole(authentication, Rol.ADMINISTRADOR);
    }

    private boolean isTecnico(Authentication authentication) {
        return this.hasRole(authentication, Rol.TECNICO_PRODUCCION);
    }

    private boolean isLector(Authentication authentication) {
        return this.hasRole(authentication, Rol.LECTOR);
    }
    
    private boolean hasStatisticalOperationInAnyRole(Authentication authentication, String statisticalOperationCode) {
        return authentication.getAuthorities().stream().anyMatch(authority -> {
            String[] appRole = authority.getAuthority().split(SecurityUtils.SEPARATOR);
            if (appRole.length < 3) {
                return true; // Se asume que no definir operaciones estadísticas para el rol te permite acceder a todas
            }
            String statisticalOperationRoleCode = appRole[2];
            return statisticalOperationCode.equals(statisticalOperationRoleCode);
        });
    }
    
    private boolean hasStatisticalOperationInRole(Authentication authentication, Rol role, String statisticalOperationCode) {
        return authentication.getAuthorities().stream().anyMatch(authority -> {
            String[] appRole = authority.getAuthority().split(SecurityUtils.SEPARATOR);
            String roleName = appRole[1];
            if (role.name().equals(roleName)) {
                if (appRole.length < 3) {
                    return true; // Se asume que no definir operaciones estadísticas para el rol te permite acceder a todas
                }
                String statisticalOperationRoleCode = appRole[2];
                return statisticalOperationCode.equals(statisticalOperationRoleCode);
            }
            return false;
        });
    }
}
