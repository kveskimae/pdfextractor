CREATE SCHEMA extraction -- AUTHORIZATION program_api_user <- is this useful

  -- Dummy table to test rest, auto commit etc.
  CREATE TABLE IF NOT EXISTS test_domain_object
  (
    id   SERIAL PRIMARY KEY,
    word TEXT NOT NULL UNIQUE
  )

  CREATE TABLE IF NOT EXISTS security_authority
  (
    id        SERIAL PRIMARY KEY,
    authority TEXT NOT NULL UNIQUE
  )

  CREATE TABLE IF NOT EXISTS security_user
  (
    id                    SERIAL PRIMARY KEY,
    username              TEXT    NOT NULL UNIQUE,
    password              TEXT    NOT NULL,
    email                 TEXT    NOT NULL UNIQUE,
    enabled               BOOLEAN NOT NULL,
    account_non_expired     BOOLEAN NOT NULL,
    account_non_locked      BOOLEAN NOT NULL,
    credentials_non_expired BOOLEAN NOT NULL,
    account_type         TEXT NOT NULL,
    trial_limit    INTEGER NOT NULL
  )

  CREATE TABLE IF NOT EXISTS security_user_security_authority
  (
    id_security_user      INTEGER NOT NULL REFERENCES security_user (id) ON DELETE CASCADE,
    id_security_authority INTEGER NOT NULL REFERENCES security_authority (id) ON DELETE CASCADE,
    PRIMARY KEY(id_security_user, id_security_authority)
  )

  CREATE TABLE IF NOT EXISTS invoice_workflow
  (
    id            SERIAL PRIMARY KEY,
    id_security_user INTEGER NOT NULL REFERENCES security_user (id) ON DELETE CASCADE,
    state         TEXT                        NOT NULL,
    locale        TEXT                        NOT NULL,
    file_name     TEXT                        NOT NULL UNIQUE,
    insert_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    complete_time TIMESTAMP WITHOUT TIME ZONE
  )

  CREATE TABLE IF NOT EXISTS extracted_field
  (
    id                  SERIAL PRIMARY KEY,
    id_invoice_workflow INTEGER NOT NULL REFERENCES invoice_workflow (id) ON DELETE CASCADE,
    payment_field_type  TEXT    NOT NULL,
    value               TEXT    NOT NULL,
    CONSTRAINT "workflow_field_unique" UNIQUE (id_invoice_workflow, payment_field_type)
  )

  CREATE TABLE IF NOT EXISTS fingerprint
  (
    id   SERIAL PRIMARY KEY,
    p_id TEXT    NOT NULL,
    x    INTEGER NOT NULL,
    y    INTEGER NOT NULL
  )

  CREATE TABLE IF NOT EXISTS phrase_type
  (
    id                 SERIAL PRIMARY KEY,
    locale             TEXT    NOT NULL,
    payment_field_type TEXT    NOT NULL,
    key_phrase         TEXT    NOT NULL,
    comparison_part    INTEGER NOT NULL
  )

  CREATE TABLE IF NOT EXISTS setting
  (
    id           SERIAL PRIMARY KEY,
    setting_type TEXT NOT NULL UNIQUE,
    value        TEXT NOT NULL
  )

;
