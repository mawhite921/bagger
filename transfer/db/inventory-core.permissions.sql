CREATE ROLE package_modeler_data_writer_role NOSUPERUSER NOINHERIT NOCREATEDB NOCREATEROLE;
GRANT CONNECT ON DATABASE package_modeler TO package_modeler_data_writer_role;
GRANT USAGE ON SCHEMA core TO package_modeler_data_writer_role;
GRANT USAGE ON SCHEMA agent TO package_modeler_data_writer_role;
GRANT SELECT ON TABLE agent.agent TO GROUP package_modeler_data_writer_role;
GRANT SELECT ON TABLE agent.agent_role TO GROUP package_modeler_data_writer_role;
GRANT SELECT ON TABLE agent."role" TO GROUP package_modeler_data_writer_role;
GRANT SELECT ON TABLE core.repository TO GROUP package_modeler_data_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE core.canonicalfile TO GROUP package_modeler_data_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE core.canonicalfile_fixity TO GROUP package_modeler_data_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE core.event_file_examination_group TO GROUP package_modeler_data_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE core.event_file_location TO GROUP package_modeler_data_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE core.event_package TO GROUP package_modeler_data_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE core.external_filelocation TO GROUP package_modeler_data_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE core.fileexamination TO GROUP package_modeler_data_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE core.fileexamination_fixity TO GROUP package_modeler_data_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE core.fileexamination_group TO GROUP package_modeler_data_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE core.fileinstance TO GROUP package_modeler_data_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE core.fileinstance_fixity TO GROUP package_modeler_data_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE core.filelocation TO GROUP package_modeler_data_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE core.package TO GROUP package_modeler_data_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE core.storagesystem_filelocation TO GROUP package_modeler_data_writer_role;

CREATE ROLE package_modeler_fixture_writer_role NOSUPERUSER NOINHERIT NOCREATEDB NOCREATEROLE;
GRANT CONNECT ON DATABASE package_modeler TO package_modeler_fixture_writer_role;
GRANT USAGE ON SCHEMA core TO package_modeler_fixture_writer_role;
GRANT USAGE ON SCHEMA agent TO package_modeler_fixture_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE agent.agent TO GROUP package_modeler_fixture_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE agent.agent_role TO GROUP package_modeler_fixture_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE agent."role" TO GROUP package_modeler_fixture_writer_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE core.repository TO GROUP package_modeler_fixture_writer_role;

CREATE ROLE package_modeler_reader_role NOSUPERUSER NOINHERIT NOCREATEDB NOCREATEROLE;
GRANT CONNECT ON DATABASE package_modeler TO package_modeler_reader_role;
GRANT USAGE ON SCHEMA core TO package_modeler_reader_role;
GRANT USAGE ON SCHEMA agent TO package_modeler_reader_role;
GRANT SELECT ON TABLE agent.agent TO GROUP package_modeler_reader_role;
GRANT SELECT ON TABLE agent.agent_role TO GROUP package_modeler_reader_role;
GRANT SELECT ON TABLE agent."role" TO GROUP package_modeler_reader_role;
GRANT SELECT ON TABLE core.repository TO GROUP package_modeler_reader_role;
GRANT SELECT ON TABLE core.canonicalfile TO GROUP package_modeler_reader_role;
GRANT SELECT ON TABLE core.canonicalfile_fixity TO GROUP package_modeler_reader_role;
GRANT SELECT ON TABLE core.event_file_examination_group TO GROUP package_modeler_reader_role;
GRANT SELECT ON TABLE core.event_file_location TO GROUP package_modeler_reader_role;
GRANT SELECT ON TABLE core.event_package TO GROUP package_modeler_reader_role;
GRANT SELECT ON TABLE core.external_filelocation TO GROUP package_modeler_reader_role;
GRANT SELECT ON TABLE core.fileexamination TO public;
GRANT SELECT ON TABLE core.fileexamination_fixity TO GROUP package_modeler_reader_role;
GRANT SELECT ON TABLE core.fileexamination_group TO GROUP package_modeler_reader_role;
GRANT SELECT ON TABLE core.fileinstance TO GROUP package_modeler_reader_role;
GRANT SELECT ON TABLE core.fileinstance_fixity TO GROUP package_modeler_reader_role;
GRANT SELECT ON TABLE core.filelocation TO GROUP package_modeler_reader_role;
GRANT SELECT ON TABLE core.package TO GROUP package_modeler_reader_role;
GRANT SELECT ON TABLE core.storagesystem_filelocation TO GROUP package_modeler_reader_role;

GRANT ALL ON TABLE hibernate_sequence TO public;
