# Invoice extractor web and RESTful API application project

## About

This Maven module **restapi** contains code for homepage and serves extraction on world wide web. It is implemented mainly with Spring and AngularJS frameworks.

## Running setup

Web container must be instructed to provide file *application.properties* to application on classpath. If Tomcat is used, then in Tomcat configuration file *catalina.properties* add folder containing file *application.properties* to shared resources folder. For example, if you have *application.properties* at */configs/application.properties* you can point as following:
```
shared.loader=/configs
```

Contents of *application.properties* contains three values. First determines if the received invoices should be saved, the second invoices save folder location, and the third IP address for querying invoice workflows:
```
SAVE_INVOICES=true
SAVE_LOCATION=/invoices
SELF_HOST_ADDRESS=1.2.3.4
```

By default, log gets written to *[tomcat base]/logs/upload.log*

## Rebuilding AngularJS Back Office application

Change source code only inside *[pdfextractor]/restapi/rdash-angular/src/*

For recompiling, inside *rdash-angular* use Gulp build tool:
```
[pdfextractor]/restapi/rdash-angular $ gulp build
```

As the result, we'll be changing contents of *[pdfextractor]/restapi/src/main/webapp/admin/*

## Deploying to live

First upload WAR file to VPS with a command similar to following:
```
scp -i /Users/[yourname]/.ssh/id_rsa [workspace]/pdfextractor/restapi/target/restapi-[version].war root@mydomain.com:/tmp/restapi-[version].war
```

Log in VPS over SSH. Copy WAR to container's *webapps* folder:

```
 cp /tmp/restapi-[version].war /tomcat/webapps/restapi-[version].war
```

Point context to extracted folder. Below is example snippet for *[tomcat-base]/conf/server.xml*:
```
  <Host name="mydomain.com"  appBase="webapps" unpackWARs="true" autoDeploy="false">
        <Context path="" docBase="restapi-[version]"/>
   </Host>
```

Sample Tomcat config files are in *tomcat-conf* folder.

For easier time, you can make a script *~/update.sh* :

```
/tomcat/bin/shutdown.sh
rm -rf /tomcat/webapps/restapi-[version]/ /tomcat/webapps/restapi-[version].war
cp /tmp/restapi-[version].war /tomcat/webapps/restapi-[version].war
/tomcat/bin/startup.sh
```

## Additional resources

+ [Installing Tomcat on Mac](https://wolfpaulus.com/journal/mac/tomcat8/)
+ [Setting context path in Tomcat](http://stackoverflow.com/questions/7276989/howto-set-the-context-path-of-a-web-application-in-tomcat-7-0) (See also example *server.xml* at *restapi/tomcat-conf/server.xml*)
+ [Preventing double deploy in Tomcat](http://www.coderanch.com/t/644458/Tomcat/Tomcat-deploying-application)

