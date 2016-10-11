-- accounts

INSERT INTO extraction.security_user (username, password, email, enabled, account_non_expired, account_non_locked, credentials_non_expired, account_type, trial_limit)
VALUES ('clientaccount', 'aaaA1', 'client@example.com', TRUE, TRUE, TRUE, TRUE, 'REGULAR', 20);

INSERT INTO extraction.security_user (username, password, email, enabled, account_non_expired, account_non_locked, credentials_non_expired, account_type, trial_limit)
VALUES ('revieweraccount', 'aaaA1', 'reviewer@example.com', TRUE, TRUE, TRUE, TRUE, 'REGULAR', 20);

-- authorities

INSERT INTO extraction.security_authority (authority)
VALUES ('CLIENT');

INSERT INTO extraction.security_authority (authority)
VALUES ('REVIEWER');

INSERT INTO extraction.security_authority (authority)
VALUES ('ADMIN');

-- examples of granting access rights

-- sample client

INSERT INTO extraction.security_user_security_authority (id_security_user, id_security_authority)
VALUES ((SELECT id FROM extraction.security_user WHERE username='clientaccount'), (SELECT id FROM extraction.security_authority WHERE authority='REVIEWER'));

INSERT INTO extraction.security_user_security_authority (id_security_user, id_security_authority)
VALUES ((SELECT id FROM extraction.security_user WHERE username='clientaccount'), (SELECT id FROM extraction.security_authority WHERE authority='CLIENT'));

-- sample internal reviewer & admin

INSERT INTO extraction.security_user_security_authority (id_security_user, id_security_authority)
VALUES ((SELECT id FROM extraction.security_user WHERE username='revieweraccount'), (SELECT id FROM extraction.security_authority WHERE authority='ADMIN'));

INSERT INTO extraction.security_user_security_authority (id_security_user, id_security_authority)
VALUES ((SELECT id FROM extraction.security_user WHERE username='revieweraccount'), (SELECT id FROM extraction.security_authority WHERE authority='REVIEWER'));
