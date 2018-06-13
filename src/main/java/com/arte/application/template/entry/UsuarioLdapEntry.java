package com.arte.application.template.entry;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import com.arte.application.template.config.ApplicationProperties;
import com.arte.application.template.optimistic.ApplicationContextProvider;

@Entry(objectClasses = {"person"})
public final class UsuarioLdapEntry {

    @Id
    private Name dn;

    @Attribute(name = "uid")
    private String nombreUsuarioUid;

    @Attribute(name = "sAMAccountName")
    private String nombreUsuarioSAMAccountName;

    @Attribute(name = "cn")
    private String nombreUsuarioCN;

    @Attribute(name = "givenName")
    private String nombre;

    @Attribute(name = "sn")
    private String apellidos;

    @Attribute(name = "sn1")
    private String apellido1;

    @Attribute(name = "sn2")
    private String apellido2;

    @Attribute(name = "mail")
    private String correoElectronico;

    @Attribute(name = "telephoneNumber")
    private String telefono;

    public Name getDn() {
        return dn;
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }
    
    public void setNombreUsuarioCN(String nombreUsuarioCN) {
        this.nombreUsuarioCN = nombreUsuarioCN;
    }

    public void setNombreUsuarioSAMAccountName(String nombreUsuarioSAMAccountName) {
        this.nombreUsuarioSAMAccountName = nombreUsuarioSAMAccountName;
    }

    public void setNombreUsuarioUid(String nombreUsuarioUid) {
        this.nombreUsuarioUid = nombreUsuarioUid;
    }

    public String getNombreUsuario() {
        final String searchUsersProperty = ApplicationContextProvider.AppContext.getApplicationContext().getBean(ApplicationProperties.class).getLdap().getSearchUsersProperty();
        if ("sAMAccountName".equals(searchUsersProperty)) {
            return nombreUsuarioSAMAccountName;
        } else if ("cn".equals(searchUsersProperty)) {
            return nombreUsuarioCN;
        } else if ("uid".equals(searchUsersProperty)) {
            return nombreUsuarioUid;
        }
        return null;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
