-- Crea el rol y se le asigna todos las operaciones
SELECT add_rol_with_all_operaciones('ADMIN', 'Adminisrador');

-- Crea un usuario y se le asigna un rol existente
SELECT add_usuario_with_existing_rol('admin', 'Administrador', 'Administrador', NULL, 'admin@localhost', 'ADMIN');
