# PDF invoices extractor parent project

## About

This Java application extracts payment data from invoices.

## Maven project layout

This is a Maven parent project with additional dependencies on open-source frameworks. The code is divided between Maven modules by functionality.

Web and RESTful API project module **restapi** faces the Internet and serves admin site together with RESTful endpoint on world wide web.

## Setup guideline

The setup requires following steps:
* Install Postgres database management system and set up database *pdfextractor* using scripts provided in folder *[pdfextractor]/db/src/main/resources/sql/*  (see README in project *db* );
* Create folder */invoices* and make it readable/writable;
* Create folder */config* and inside it file *application.properties* , see example in *[pdfextractor]/restapi/application.properties* ;
* Set up Tomcat and make data source *pdfextractorDataSource* available to deployed applications, example *context.xml* and *server.xml* are in *[pdfextractor]/restapi/tomcat-conf* ;
* Package module *restapi* as WAR-file and deploy inside Tomcat.