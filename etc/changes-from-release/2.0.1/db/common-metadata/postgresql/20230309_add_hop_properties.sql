-- --------------------------------------------------------------------------------------------------
-- EDATOS-3989 - Integración con Apache Hop
-- --------------------------------------------------------------------------------------------------

-- CoETL

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.auth.password', 'FILL_ME_WITH_HOP_PASSWORD', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.auth.password', 'zCQgN1hIAvFgdNQo3U7V72+K3uBnoNCA', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.auth.user', 'FILL_ME_WITH_HOP_USER', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.auth.user', 'admin', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.endpoint', 'FILL_ME_WITH_HOP_API_ENDPOINT', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO:INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.endpoint', 'http://192.168.10.105:9595/hop/', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.address', 'FILL_ME_WITH_SERVER_ADDRESSS', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.address', 'localhost', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.os', 'FILL_ME_WITH_OS_SERVER', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.os', 'UNIX', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.ownerGroupResourcesPath', 'FILL_ME_WITH_SERVER_GROUP_RESOURCES_PATH', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.ownerGroupResourcesPath', 'pentaho', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.ownerUserResourcesPath', 'FILL_ME_WITH_SERVER_USER_RESOURCES_PATH', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.ownerUserResourcesPath', 'pentaho', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.password', 'FILL_ME_WITH_SERVER_USER_PASSWORD', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.password', 'IZTBIWbsL0284Xdcb2mZ2gazc21Laxwt', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.resourcesPath', 'FILL_ME_WITH_RESOURCES_PATH', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.resourcesPath', '/home/pentaho/resources', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.sudo.password', 'FILL_ME_WITH_SERVER_USER_SUDO_PASSWORD', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.sudo.password', 'IZTBIWbsL0284Xdcb2mZ2gazc21Laxwt', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.sudo.username', 'FILL_ME_WITH_SERVER_USER_SUDO', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.sudo.username', 'root', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.mainResourcePrefix', 'FILL_WITH_HOP_MAIN_RESOURCE_PREFIX', true, true, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.mainResourcePrefix', 'main', true, true, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.username', 'FILL_ME_WITH_SERVER_USER', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.username', 'arte', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.jsonMetadata', 'FILL_WITH_HOP_JSON_METADATA', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.jsonMetadata', '{"server":[{"webAppName":"","sslConfig":null,"sslMode":false,"proxyPort":"","hostname":"127.0.0.1","password":"admin","nonProxyHosts":"","overrideExistingProperties":false,"propertiesMasterName":null,"port":"8081","name":"new","proxyHostname":"","username":"admin"}],"pipeline-probe":[],"unit-test":[],"rdbms":[],"workflow-log":[],"cassandra-connection":[],"neo4j-graph-model":[],"web-service":[{"transformName":"Dummy (do nothing)","filename":"\/Users\/hans\/test\/dataService.hpl","fieldName":"value","listingStatus":false,"name":"sample_webservice","contentType":"application\/json","enabled":true}],"pipeline-run-configuration":[{"engineRunConfiguration":{"Local":{"feedback_size":"50000","sample_size":"100","sample_type_in_gui":"Last","rowset_size":"10000","safe_mode":false,"show_feedback":false,"topo_sort":false,"gather_metrics":false,"transactional":false}},"configurationVariables":[],"name":"local","description":"Runs your pipelines locally with the standard local Hop pipeline engine"}],"neo4j-connection":[],"mongodb-connection":[],"partition":[],"async-web-service":[],"pipeline-log":[],"workflow-run-configuration":[{"engineRunConfiguration":{"Local":{"safe_mode":false,"transactional":false}},"name":"local2","description":"Runs your workflows locally with the standard local Hop workflow engine"},{"engineRunConfiguration":{"Local":{"safe_mode":false,"transactional":false}},"name":"local","description":"Runs your workflows locally with the standard local Hop workflow engine"}],"file-definition":[{"enclosure":"","name":"new","description":"","fieldDefinitions":[],"separator":""}],"splunk":[],"dataset":[]}', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.pentaho.host.sudoPasswordPromptRegex', 'FILL_WITH_PASSWORD_REGEX', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.hop.host.sudoPasswordPromptRegex', '.*[Pp]assword.*', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


-- CoETL LAB

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.auth.password', 'FILL_ME_WITH_HOP_PASSWORD', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.auth.password', 'zCQgN1hIAvFgdNQo3U7V72+K3uBnoNCA', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.auth.user', 'FILL_ME_WITH_HOP_USER', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.auth.user', 'admin', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.endpoint', 'FILL_ME_WITH_HOP_API_ENDPOINT', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO:INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.endpoint', 'http://192.168.10.105:9595/hop/', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.address', 'FILL_ME_WITH_SERVER_ADDRESSS', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.address', 'localhost', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.os', 'FILL_ME_WITH_OS_SERVER', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.os', 'UNIX', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.ownerGroupResourcesPath', 'FILL_ME_WITH_SERVER_GROUP_RESOURCES_PATH', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.ownerGroupResourcesPath', 'pentaho', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.ownerUserResourcesPath', 'FILL_ME_WITH_SERVER_USER_RESOURCES_PATH', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.ownerUserResourcesPath', 'pentaho', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.password', 'FILL_ME_WITH_SERVER_USER_PASSWORD', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.password', 'IZTBIWbsL0284Xdcb2mZ2gazc21Laxwt', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.resourcesPath', 'FILL_ME_WITH_RESOURCES_PATH', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.resourcesPath', '/home/pentaho/resources_coetllab', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.sudo.password', 'FILL_ME_WITH_SERVER_USER_SUDO_PASSWORD', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.sudo.password', 'IZTBIWbsL0284Xdcb2mZ2gazc21Laxwt', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.sudo.username', 'FILL_ME_WITH_SERVER_USER_SUDO', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.sudo.username', 'root', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.mainResourcePrefix', 'FILL_WITH_HOP_MAIN_RESOURCE_PREFIX', true, true, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.mainResourcePrefix', 'main', true, true, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.username', 'FILL_ME_WITH_SERVER_USER', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.username', 'arte', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.jsonMetadata', 'FILL_WITH_HOP_JSON_METADATA', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.jsonMetadata', '{"server":[{"webAppName":"","sslConfig":null,"sslMode":false,"proxyPort":"","hostname":"127.0.0.1","password":"admin","nonProxyHosts":"","overrideExistingProperties":false,"propertiesMasterName":null,"port":"8081","name":"new","proxyHostname":"","username":"admin"}],"pipeline-probe":[],"unit-test":[],"rdbms":[],"workflow-log":[],"cassandra-connection":[],"neo4j-graph-model":[],"web-service":[{"transformName":"Dummy (do nothing)","filename":"\/Users\/hans\/test\/dataService.hpl","fieldName":"value","listingStatus":false,"name":"sample_webservice","contentType":"application\/json","enabled":true}],"pipeline-run-configuration":[{"engineRunConfiguration":{"Local":{"feedback_size":"50000","sample_size":"100","sample_type_in_gui":"Last","rowset_size":"10000","safe_mode":false,"show_feedback":false,"topo_sort":false,"gather_metrics":false,"transactional":false}},"configurationVariables":[],"name":"local","description":"Runs your pipelines locally with the standard local Hop pipeline engine"}],"neo4j-connection":[],"mongodb-connection":[],"partition":[],"async-web-service":[],"pipeline-log":[],"workflow-run-configuration":[{"engineRunConfiguration":{"Local":{"safe_mode":false,"transactional":false}},"name":"local2","description":"Runs your workflows locally with the standard local Hop workflow engine"},{"engineRunConfiguration":{"Local":{"safe_mode":false,"transactional":false}},"name":"local","description":"Runs your workflows locally with the standard local Hop workflow engine"}],"file-definition":[{"enclosure":"","name":"new","description":"","fieldDefinitions":[],"separator":""}],"splunk":[],"dataset":[]}', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetl.pentaho.host.sudoPasswordPromptRegex', 'FILL_WITH_PASSWORD_REGEX', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
-- Ejemplo DEMO: INSERT INTO tb_data_configurations (id, conf_key, conf_value, system_property, externally_published, update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version")
-- VALUES(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 'metamac.coetllab.hop.host.sudoPasswordPromptRegex', '.*[Pp]assword.*', true, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


COMMIT;
