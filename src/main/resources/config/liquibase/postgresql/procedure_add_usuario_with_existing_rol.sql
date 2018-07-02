CREATE OR REPLACE FUNCTION add_usuario_with_existing_rol(login usuario.login%TYPE, nombre usuario.nombre%TYPE, apellido1 usuario.apellido1%TYPE, 
                                                         apellido2 usuario.apellido2%TYPE, email usuario.email%TYPE, codigoRol usuario_rol.rol%TYPE) 
    RETURNS void AS $$
    BEGIN
        INSERT INTO usuario(id, opt_lock, login, nombre, apellido1, apellido2, email, deletion_date, created_by, created_date, last_modified_by, last_modified_date)
            VALUES(nextval('usuario_id_seq'), 0, login, nombre, apellido1, apellido2, email, NULL, 'system', now(), 'system', NULL);

        INSERT INTO usuario_rol(usuario_id, rol)
            VALUES(currval('usuario_id_seq'), codigoRol);
    
        raise notice 'Done!';
    
    EXCEPTION
        WHEN unique_violation THEN
            raise notice 'Se ha violado una constraint única. ERROR -> %', SQLERRM;
        WHEN OTHERS THEN
            raise notice 'Algo ha ocurrido mal. ERROR -> %', SQLERRM;
    END;
    $$ LANGUAGE plpgsql;
    