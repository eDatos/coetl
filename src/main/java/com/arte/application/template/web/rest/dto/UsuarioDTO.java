package com.arte.application.template.web.rest.dto;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.arte.application.template.config.Constants;
import com.arte.application.template.domain.enumeration.Rol;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UsuarioDTO {

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 255)
    private String login;

    private Long optLock;

    @Size(max = 255)
    private String nombre;

    @Size(max = 255)
    private String apellido1;

    @Size(max = 255)
    private String apellido2;

    @Email
    @Size(min = 3, max = 255)
    private String email;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private SortedSet<Rol> roles;

    private ZonedDateTime deletionDate;

    public UsuarioDTO() {
        // Empty constructor needed for Jackson.
    }

    public void updateFrom(UsuarioDTO source) {
        this.id = source.getId();
        this.optLock = source.getOptLock();
        this.login = source.getLogin();
        this.nombre = source.getNombre();
        this.apellido1 = source.getApellido1();
        this.apellido2 = source.getApellido2();
        this.email = source.getEmail();
        this.createdBy = source.getCreatedBy();
        this.createdDate = source.getCreatedDate();
        this.lastModifiedBy = source.getLastModifiedBy();
        this.lastModifiedDate = source.getLastModifiedDate();
        this.roles = new TreeSet<>(source.getRoles());
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOptLock() {
        return optLock;
    }

    public void setOptLock(Long optLock) {
        this.optLock = optLock;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public String getEmail() {
        return email;
    }

    @JsonProperty
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty
    public Instant getCreatedDate() {
        return createdDate;
    }

    @JsonProperty
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    @JsonProperty
    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> set) {
        if (set == null) {
            this.roles = new TreeSet<>();
        } else {
            this.roles = new TreeSet<>(set);
        }
    }

    @JsonIgnore
    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @JsonIgnore
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;

    }

    @JsonIgnore
    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;

    }

    @JsonIgnore
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setApellido1(String lastName) {
        this.apellido1 = lastName;
    }

    public void setApellido2(String lastName) {
        this.apellido2 = lastName;
    }

    public void setNombre(String firstName) {
        this.nombre = firstName;
    }

    @JsonProperty
    public ZonedDateTime getDeletionDate() {
        return deletionDate;
    }

    @JsonIgnore
    public void setDeletionDate(ZonedDateTime deletionDate) {
        this.deletionDate = deletionDate;
    }

    @Override
    public String toString() {
        return "UsuarioDTO (id = " + getId() + ", Nombre = " + getNombre() + ", Apellido1 = " + getApellido1() + ", Apellido2 = " + getApellido2() + ")";
    }

    public static class Builder {

        private Long id;
        private String login;
        private String firstName;
        private String lastName;
        private String lastName2;
        private String email;
        private String createdBy;
        private Instant createdDate;
        private String lastModifiedBy;
        private Instant lastModifiedDate;
        private Set<Rol> authorities;
        private Long optLock;

        public UsuarioDTO build() {
            UsuarioDTO userDTO = new UsuarioDTO();
            userDTO.setId(this.id);
            userDTO.setOptLock(this.optLock);
            userDTO.setLogin(this.login);
            userDTO.setNombre(this.firstName);
            userDTO.setApellido1(this.lastName);
            userDTO.setApellido2(this.lastName2);
            userDTO.setEmail(this.email);
            userDTO.setCreatedBy(this.createdBy);
            userDTO.setCreatedDate(this.createdDate);
            userDTO.setLastModifiedBy(this.lastModifiedBy);
            userDTO.setLastModifiedDate(this.lastModifiedDate);
            userDTO.setRoles(this.authorities);
            return userDTO;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setOptLock(Long optLock) {
            this.optLock = optLock;
            return this;
        }

        public Builder setLogin(String login) {
            this.login = login;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setLastName2(String lastName) {
            this.lastName2 = lastName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder setCreatedDate(Instant createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder setLastModifiedBy(String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
            return this;
        }

        public Builder setLastModifiedDate(Instant lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
            return this;
        }

        public Builder setAuthorities(Set<Rol> authorities) {
            this.authorities = authorities;
            return this;
        }

    }
}
