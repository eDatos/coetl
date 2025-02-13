-- --------------------------------------------------------------------------------------------------
-- EDATOS-4828 - Integración de hop en coetl con base de datos
-- --------------------------------------------------------------------------------------------------

-- CoETL

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.hopFolder', 'FILL_ME_WITH_HOP_FOLDER', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.hopFolder', '/servers/hop/hop', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.variables', 'FILL_ME_WITH_HOP_VARIABLES', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.variables', '<variables><variable><name>HOP_AUTO_CREATE_CONFIG</name><value>Y</value></variable><variable><name>HOP_METADATA_FOLDER</name><value>${ETL_RESOURCES}/metadata</value></variable><variable><name>HOP_PROJECT_NAME</name><value>${ETL_CODE}</value></variable><variable><name>PROJECT_HOME</name><value>${HOP_FOLDER}/config/projects/${ETL_CODE}</value></variable></variables>', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


-- CoETL LAB

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.hopFolder', 'FILL_ME_WITH_HOP_FOLDER', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.hopFolder', '/servers/hop/hop', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.variables', 'FILL_ME_WITH_HOP_VARIABLES', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.variables', '<variables><variable><name>HOP_AUTO_CREATE_CONFIG</name><value>Y</value></variable><variable><name>HOP_METADATA_FOLDER</name><value>${ETL_RESOURCES}/metadata</value></variable><variable><name>HOP_PROJECT_NAME</name><value>${ETL_CODE}</value></variable><variable><name>PROJECT_HOME</name><value>${HOP_FOLDER}/config/projects/${ETL_CODE}</value></variable></variables>', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

COMMIT;
