package es.gobcan.coetl.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;

import es.gobcan.coetl.config.Constants;
import es.gobcan.coetl.domain.enumeration.Rol;

@Entity
@Table(name = "usuario")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Usuario extends AbstractVersionedAndAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_id_seq")
    @SequenceGenerator(name = "usuario_id_seq", sequenceName = "usuario_id_seq", allocationSize = 50, initialValue = 10)
    private Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 255)
    @Column(length = 255, unique = true, nullable = false)
    private String login;

    @Size(max = 255)
    @Column(name = "nombre", length = 255)
    private String nombre;

    @Size(max = 255)
    @Column(name = "apellido1", length = 255)
    private String apellido1;

    @Size(max = 255)
    @Column(name = "apellido2", length = 255)
    private String apellido2;

    @Email
    @Size(min = 3, max = 255)
    @Column(length = 255)
    private String email;

    @ElementCollection(targetClass = Rol.class)
    @JoinTable(name = "usuario_rol", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"))
    @Column(name = "rol", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Rol> roles = new HashSet<>();

    @Column(name = "deletion_date")
    private ZonedDateTime deletionDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = StringUtils.lowerCase(login);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String firstName) {
        this.nombre = firstName;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String lastName) {
        this.apellido1 = lastName;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String lastName) {
        this.apellido2 = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    public ZonedDateTime getDeletionDate() {
        return deletionDate;
    }

    public void setDeletionDate(ZonedDateTime deletionDate) {
        this.deletionDate = deletionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Usuario user = (Usuario) o;
        return !(user.getId() == null || getId() == null) && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Usuario (id = " + getId() + ", Nombre = " + getNombre() + ", Apellido1 = " + getApellido1() + ", Apellido2 = " + getApellido2() + ")";
    }
}
