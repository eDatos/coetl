CREATE OR REPLACE FUNCTION add_rol_with_all_operaciones(codigo rol.codigo%TYPE, nombre rol.nombre%TYPE) 
    RETURNS void AS $$
    DECLARE
        idOperacion int8;
    BEGIN
        
        INSERT INTO rol(id, opt_lock, codigo, nombre)
            VALUES(nextval('rol_id_seq'), 0, codigo, nombre);

        FOR idOperacion IN SELECT id FROM operacion
        LOOP
            INSERT INTO roles_operaciones(rol_id, operacion_id)
                VALUES(currval('rol_id_seq'), idOperacion);
        END LOOP;
    
        raise notice 'Done!';
        
    EXCEPTION
        WHEN unique_violation THEN
            raise notice 'Se ha violado una constraint única. ERROR -> %', SQLERRM;
        WHEN OTHERS THEN
            raise notice 'Algo ha ocurrido mal. ERROR -> %', SQLERRM;
    END;
    $$ LANGUAGE plpgsql;
    