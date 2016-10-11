
CREATE DATABASE pdfextractor;

\c pdfextractor

CREATE USER program_api_user WITH PASSWORD 'changeme';

GRANT CONNECT ON DATABASE pdfextractor TO program_api_user;