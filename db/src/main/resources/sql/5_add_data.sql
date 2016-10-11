-- ESTONIA

INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('et', 'INVOICE_ID', 'arve', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('et', 'INVOICE_ID', 'invoice', 3);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('et', 'INVOICE_ID', 'pakkumine', 1);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('et', 'INVOICE_ID', 'ettemaksu arve', 1);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('et', 'INVOICE_ID', 'arve/saateleht', 1);

INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('et', 'NAME', 'OÜ', 3);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('et', 'NAME', 'AS', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('et', 'NAME', 'MTÜ', 1);

INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('et', 'TOTAL', 'kuulub tasumisele', 1);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('et', 'TOTAL', 'tasumisele kuulub', 1);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('et', 'TOTAL', 'tasumisele kuuluv summa', 1);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('et', 'TOTAL', 'tasuda', 1);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('et', 'TOTAL', 'arve kokku', 1);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('et', 'TOTAL', 'kokku', 1);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('et', 'TOTAL', 'summa', 1);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('et', 'TOTAL', 'total', 1);

-- ITALY

INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'INVOICE_ID', 'fattura', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'INVOICE_ID', 'document', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'INVOICE_ID', 'doc\\.', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'INVOICE_ID', 'Invoice Number', 5);

INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'ISSUE_DATE', 'data', 5);

INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 'soc[.]{0,1}', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 'org[.]{0,1}', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 'S[.]{0,1}(a[.]{0,1})?p[.]{0,1}A[.]{0,1}', 5); -- Società per Azioni
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 'S[.]{0,1}r[.]{0,1}l[.]{0,1}s[.]{0,1}', 5); -- società a responsabilità limitata semplificata
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 'S[.]{0,1}r[.]{0,1}l[.]{0,1}', 5); -- società a responsabilità limitata
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 'coop[.]{0,1}', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 's[.]{0,1}a[.]{0,1}s[.]{0,1}', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 'inc[.]{0,1}', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 'ltd[.]{0,1}', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 'limited', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 'Società', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 'azienda', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 'agenzia', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 'compagnia', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 'ditta', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 'cooperativa', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 'studio legale', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'NAME', 'studio medico', 5);

INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL_BEFORE_TAXES', 'IMPONIBILE', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL_BEFORE_TAXES', 'Totale imponibile', 5);

INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL', 'Netto a pagarsi', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL', 'Totale da pagare', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL', 'Totale documento', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL', 'Totale fattura', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL', 'Ammontare da pagare', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL', 'Ammontare', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL', 'Ammontare pagabile', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL', 'Ammontare totale', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL', 'Importo pagabile', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL', 'Importo totale', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL', 'Importo documento', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL', 'Importo fattura', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL', 'Importo', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL', 'Da pagare', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL', 'totale addebito', 5);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL', 'totale', 3);
INSERT INTO extraction.phrase_type (locale, payment_field_type, key_phrase, comparison_part)
VALUES ('it', 'TOTAL', 'total', 1);

-- Change these
INSERT INTO extraction.setting (setting_type, value) VALUES ('API_EMAIL_USERNAME', 'noonehere@gmail.com');
INSERT INTO extraction.setting (setting_type, value) VALUES ('API_EMAIL_PASSWORD', 'qwerty');