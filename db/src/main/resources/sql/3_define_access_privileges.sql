-- SCHEMA

GRANT USAGE ON SCHEMA extraction TO program_api_user;

-- TABLES

GRANT ALL ON extraction.test_domain_object TO program_api_user;

GRANT SELECT, INSERT, UPDATE ON extraction.invoice_workflow TO program_api_user;

GRANT SELECT, INSERT, UPDATE, DELETE ON extraction.extracted_field TO program_api_user; -- TODO For security, grant delete to another Postgres user instead of program_api_user?

GRANT SELECT, INSERT, UPDATE, DELETE ON extraction.fingerprint TO program_api_user;

GRANT SELECT, INSERT, UPDATE, DELETE ON extraction.phrase_type TO program_api_user;

GRANT SELECT, INSERT, UPDATE, DELETE ON extraction.security_authority TO program_api_user;

GRANT SELECT, INSERT, UPDATE, DELETE ON extraction.security_user TO program_api_user;

GRANT SELECT, INSERT, UPDATE, DELETE ON extraction.security_user_security_authority TO program_api_user;

GRANT SELECT, INSERT, UPDATE, DELETE ON extraction.setting TO program_api_user;

-- SEQUENCES

GRANT USAGE, SELECT ON extraction.test_domain_object_id_seq TO program_api_user;

GRANT USAGE, SELECT ON extraction.extracted_field_id_seq TO program_api_user;

GRANT USAGE, SELECT ON extraction.invoice_workflow_id_seq TO program_api_user;

GRANT USAGE, SELECT ON extraction.fingerprint_id_seq TO program_api_user;

GRANT USAGE, SELECT ON extraction.phrase_type_id_seq TO program_api_user;

GRANT USAGE, SELECT ON extraction.security_authority_id_seq TO program_api_user;

GRANT USAGE, SELECT ON extraction.security_user_id_seq TO program_api_user;

GRANT USAGE, SELECT ON extraction.setting_id_seq TO program_api_user;
