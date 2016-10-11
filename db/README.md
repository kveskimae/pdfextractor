Data access layer
==========================

This project provides data storage to its dependants. 

Getting started
---------------

### Naming conventions

Sequences for primary keys are named 

`<table_name>_<primare_key_name>_seq`

Setup
---------------

### Installing database

Database is Postgres. Creation SQLs are located in
`[pdfextractor]/db/src/main/resources/sql/*.sql`

Database connectivity is provided by container.

For username/password setup see configuration for JNDI resource named *jdbc/pdfextractorDataSource_GLOBAL* in

`[pdfextractor]/restapi/src/main/tomcat-config/server.xml`

and

`[pdfextractor]/restapi/src/main/tomcat-config/context.xml`

These two files are provided as examples to use inside Tomcat container configurations folder `[tomcat]/conf/`, e.g. `[tomcat]/conf/context.xml`

Troubleshooting
---------------

#### Test if Postgres is running

Start/stop Postgres

`sudo service postgresql start`

`sudo service postgresql stop`

Verify liveness

`ps auxwww | grep postgres`

#### Connecting to Postgres from bash

`sudo -i`

`su postgres`

`psql -h localhost -U postgres -d pdfextractor`

`\c pdfextractor`

#### Checking access rights

Connect and issue

`SELECT * FROM information_schema.role_table_grants WHERE grantee='pgtest';`

#### Checking connectivity

Tomcat config *server.xml* has validation query for JNDI data resource *jdbc/pdfextractorDataSource* used by this layer.

#### Some useful SQL and Postgres tips

Make Postgres accessible from outside the remote server:

http://www.cyberciti.biz/tips/postgres-allow-remote-access-tcp-connection.html

Describe table:

`\d+ "extraction"."Test"`

List all tables:

`\dt *.*`

List all sequences:

`SELECT c.relname FROM pg_class c WHERE c.relkind = 'S';`

Some more useful Postgres commands:

Command                                             | Action     | SQL alternative
:-------------------------------- | :------- | :-------
`\l+`                                                    | List databases with privileges                 | &nbsp;
`\dn+`                                                 | List schemas with privileges                   | select schema_name from information_schema.schemata;
`\dt+ <schema_name>.*`                     | List all tables in schema with privileges | SELECT * FROM pg_catalog.pg_tables;
&nbsp;                                                   | List all sequences                                   | SELECT c.relname FROM pg_class c WHERE c.relkind = 'S';
`\d+ <schema_name>.<table_name>` | Describe table                                       | &nbsp;
