-- EDATOS-3584 . Para una instalación dual ( COETL y COETLLAB) también será necesario añadir las propiedades de ambas instancias.

--COETL
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.db.url','jdbc:postgresql://FILL_ME_WITH_HOST:FILL_ME_WITH_PORT/FILL_ME_WITH_DB', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.db.username','FILL_ME_WITH_USER', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.db.password','FILL_ME_WITH_ENCRYPTED_PASSWORD', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.db.driver_name','org.postgresql.Driver', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.pentaho.endpoint','FILL_WITH_PENTAHO_HOST', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.pentaho.auth.user','FILL_WITH_PENTAHO_USER', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.pentaho.auth.password','FILL_WITH_PENTAHO_ENCRPTED_PASSWORD', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.pentaho.host.os','FILL_WITH_PENTAHO_HOST_OS', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.pentaho.host.address','FILL_WITH_PENTAHO_HOST_ADDRESS', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.pentaho.host.username','FILL_WITH_PENTAHO_HOST_USERNAME', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.pentaho.host.password','FILL_WITH_PENTAHO_HOST_PASSWORD', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.pentaho.host.sudo.username','FILL_WITH_PENTAHO_HOST_SUDO_USERNAME', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.pentaho.host.sudo.password','FILL_WITH_PENTAHO_HOST_SUDO_USERNAME', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.pentaho.host.sudoPasswordPromptRegex','FILL_WITH_PENTAHO_HOST_SUDO_PASSWORD_REGEX', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.pentaho.host.sftpPath','FILL_WITH_PENTAHO_HOST_SFTP_PATH', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.pentaho.host.resourcesPath','FILL_WITH_PENTAHO_HOST_RESOURCE_PATH', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.pentaho.host.ownerUserResourcesPath','FILL_WITH_PENTAHO_HOST_OWNER_RESOURCE_PATH', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.pentaho.host.ownerGroupResourcesPath','FILL_WITH_PENTAHO_HOST_OWNERGROUP_RESOURCE_PATH', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.cas.service','FILL_WITH_COETL_CAS_ENDPOINT', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.git.username','FILL_WITH_GIT_USER', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.git.password','FILL_WITH_GIT_ENCRYPTED_PASSWORD', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.git.branch','FILL_WITH_BRANCH', true);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetl.pentaho.mainResourcePrefix','FILL_WITH_MAIN_RESOURCE_PREFIX', true);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.auth.password', 'FILL_ME_WITH_HOP_PASSWORD', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.auth.user', 'FILL_ME_WITH_HOP_USER', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.endpoint', 'FILL_ME_WITH_HOP_API_ENDPOINT', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.address', 'FILL_ME_WITH_SERVER_ADDRESSS', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.os', 'FILL_ME_WITH_OS_SERVER', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.ownerGroupResourcesPath', 'FILL_ME_WITH_SERVER_GROUP_RESOURCES_PATH', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.ownerUserResourcesPath', 'FILL_ME_WITH_SERVER_USER_RESOURCES_PATH', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.password', 'FILL_ME_WITH_SERVER_USER_PASSWORD', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.resourcesPath', 'FILL_ME_WITH_RESOURCES_PATH', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.sudo.password', 'FILL_ME_WITH_SERVER_USER_SUDO_PASSWORD', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.sudo.username', 'FILL_ME_WITH_SERVER_USER_SUDO', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.mainResourcePrefix', 'FILL_WITH_HOP_MAIN_RESOURCE_PREFIX', true, true, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.username', 'FILL_ME_WITH_SERVER_USER', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.jsonMetadata', 'FILL_WITH_HOP_JSON_METADATA', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.sudoPasswordPromptRegex', '.*[Pp]assword.*', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.hopFolder', 'FILL_ME_WITH_HOP_FOLDER', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.variables', 'FILL_ME_WITH_HOP_VARIABLES', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


--COETLLAB
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.git.username','FILL_WITH_GIT_USER', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.git.password','FILL_WITH_GIT_ENCRYPTED_PASSWORD', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.pentaho.mainResourcePrefix','FILL_WITH_MAIN_RESOURCE_PREFIX', true);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.git.branch','FILL_WITH_BRANCH', true);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.pentaho.endpoint','FILL_WITH_PENTAHO_HOST', true);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.pentaho.auth.user','FILL_WITH_PENTAHO_USER', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.pentaho.auth.password','FILL_WITH_PENTAHO_ENCRPTED_PASSWORD', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.pentaho.host.os','FILL_WITH_PENTAHO_HOST_OS', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.pentaho.host.address','FILL_WITH_PENTAHO_HOST_ADDRESS', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.pentaho.host.username','FILL_WITH_PENTAHO_HOST_USERNAME', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.pentaho.host.password','FILL_WITH_PENTAHO_HOST_PASSWORD', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.pentaho.host.sudo.username','FILL_WITH_PENTAHO_HOST_SUDO_USERNAME', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.pentaho.host.sudo.password','FILL_WITH_PENTAHO_HOST_SUDO_USERNAME', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.pentaho.host.sudoPasswordPromptRegex','FILL_WITH_PENTAHO_HOST_SUDO_PASSWORD_REGEX', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.pentaho.host.sftpPath','FILL_WITH_PENTAHO_HOST_SFTP_PATH', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.pentaho.host.resourcesPath','FILL_WITH_PENTAHO_HOST_RESOURCE_PATH', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.pentaho.host.ownerUserResourcesPath','FILL_WITH_PENTAHO_HOST_OWNER_RESOURCE_PATH', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.pentaho.host.ownerGroupResourcesPath','FILL_WITH_PENTAHO_HOST_OWNERGROUP_RESOURCE_PATH', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.db.url','jdbc:postgresql://FILL_ME_WITH_HOST:FILL_ME_WITH_PORT/FILL_ME_WITH_DB', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.db.username','FILL_ME_WITH_USER', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.db.password','FILL_ME_WITH_ENCRYPTED_PASSWORD', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.db.driver_name','org.postgresql.Driver', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.coetllab.cas.service','FILL_WITH_COETLLAB_CAS_ENDPOINT', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.auth.password', 'FILL_ME_WITH_HOP_PASSWORD', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.auth.user', 'FILL_ME_WITH_HOP_USER', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.endpoint', 'FILL_ME_WITH_HOP_API_ENDPOINT', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.address', 'FILL_ME_WITH_SERVER_ADDRESSS', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.os', 'FILL_ME_WITH_OS_SERVER', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.ownerGroupResourcesPath', 'FILL_ME_WITH_SERVER_GROUP_RESOURCES_PATH', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.ownerUserResourcesPath', 'FILL_ME_WITH_SERVER_USER_RESOURCES_PATH', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.password', 'FILL_ME_WITH_SERVER_USER_PASSWORD', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.resourcesPath', 'FILL_ME_WITH_RESOURCES_PATH', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.sudo.password', 'FILL_ME_WITH_SERVER_USER_SUDO_PASSWORD', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.sudo.username', 'FILL_ME_WITH_SERVER_USER_SUDO', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.mainResourcePrefix', 'FILL_WITH_HOP_MAIN_RESOURCE_PREFIX', true, true, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.username', 'FILL_ME_WITH_SERVER_USER', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.jsonMetadata', 'FILL_WITH_HOP_JSON_METADATA', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.sudoPasswordPromptRegex', '.*[Pp]assword.*', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.hopFolder', 'FILL_ME_WITH_HOP_FOLDER', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.variables', 'FILL_ME_WITH_HOP_VARIABLES', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


commit;
