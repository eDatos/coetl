package com.arte.application.template.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.arte.application.template.ArteApplicationTemplateApp;
import com.arte.application.template.domain.Rol;
import com.arte.application.template.domain.Usuario;
import com.arte.application.template.repository.RolRepository;
import com.arte.application.template.repository.UsuarioRepository;
import com.arte.application.template.service.RolService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArteApplicationTemplateApp.class)
public class RolServiceImplTest {

    @Autowired
    RolRepository rolRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    RolService service;

    Usuario usuario;

    @Before
    public void setUp() throws Exception {
        usuario = mockUsuario();
        usuarioRepository.saveAndFlush(usuario);
        service = new RolServiceImpl(rolRepository, usuarioRepository, null, null);
    }

    private Set<Rol> mockRoles(String... rolNames) {
        Set<Rol> roles = new HashSet<>();
        for (String rolName : rolNames) {
            Rol mockRol = mockRol(rolName);
            roles.add(mockRol);
            rolRepository.save(mockRol);
        }
        return roles;
    }

    private Rol mockRol(String rolName) {
        Rol rol = rolRepository.findOneByCodigo(rolName);
        if (rol == null) {
            rol = new Rol();
            rol.setCodigo(rolName);
            rol.setNombre(rolName);
        }
        return rol;
    }

    private Usuario mockUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("login_usuario");
        return usuario;
    }

    @Test
    @Transactional
    public void findByUsuarioDevuelveRolesDelUsuario() {
        usuario.setRoles(mockRoles("usuario", "admin"));
        usuarioRepository.saveAndFlush(usuario);

        Set<Rol> roles = service.findByUsuario(usuario.getLogin());

        assertThat(roles.size()).isEqualTo(usuario.getRoles().size());
        assertThat(roles).containsAll(usuario.getRoles());
    }

    @Test
    @Transactional
    public void findByUsuarioDevuelveRolesDelUsuarioHabiendoOtrosUsuarios() {
        Usuario usuario2 = mockUsuario();
        usuario2.setId(2L);
        usuario2.setLogin("otraUsuaria");
        usuarioRepository.saveAndFlush(usuario2);
        usuario2.setRoles(mockRoles("usuario"));
        usuarioRepository.saveAndFlush(usuario2);

        usuario.setRoles(mockRoles("usuario", "admin"));
        usuarioRepository.saveAndFlush(usuario);

        Set<Rol> roles = service.findByUsuario(usuario.getLogin());

        assertThat(roles.size()).isEqualTo(usuario.getRoles().size());
        assertThat(roles).containsAll(usuario.getRoles());
    }

    @Test
    @Transactional
    public void findByUsuarioDevuelveRolesDelUsuarioHabiendoOtrosUsuariosConMasRoles() {
        Usuario usuario2 = mockUsuario();
        usuario2.setId(2L);
        usuario2.setLogin("otraUsuaria");
        usuarioRepository.saveAndFlush(usuario2);
        usuario2.setRoles(mockRoles("usuario", "gestor", "administrador", "secretario"));
        usuarioRepository.saveAndFlush(usuario2);

        usuario.setRoles(mockRoles("usuario", "admin"));
        usuarioRepository.saveAndFlush(usuario);

        Set<Rol> roles = service.findByUsuario(usuario.getLogin());

        assertThat(roles.size()).isEqualTo(usuario.getRoles().size());
        assertThat(roles).containsAll(usuario.getRoles());
        assertThat(roles).doesNotContain(mockRol("gestor"), mockRol("secretario"));
    }
}
