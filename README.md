![](https://github.com/cas-bigdatalab/piflow/blob/master/doc/piflow-logo3.png) 

[![GitHub releases](https://img.shields.io/github/release/cas-bigdatalab/piflow-web.svg)](https://github.com/cas-bigdatalab/piflow-web/releases)
[![GitHub downloads](https://img.shields.io/github/downloads/cas-bigdatalab/piflow-web/total.svg)](https://github.com/cas-bigdatalab/piflow-web/releases)
[![GitHub issues](https://img.shields.io/github/issues/cas-bigdatalab/piflow-web.svg)](https://github.com/cas-bigdatalab/piflow-web/issues)
[![GitHub forks](https://img.shields.io/github/forks/cas-bigdatalab/piflow-web.svg)](https://github.com/cas-bigdatalab/piflow-web/network)
[![GitHub stars](https://img.shields.io/github/stars/cas-bigdatalab/piflow-web.svg)](https://github.com/cas-bigdatalab/piflow-web/stargazers)
[![GitHub license](https://img.shields.io/github/license/cas-bigdatalab/piflow-web.svg)](https://github.com/cas-bigdatalab/piflow-web/blob/master/LICENSE)
## Requirements
* JDK 1.8
* MySQL 5.7
## Getting Started
To Build: mvn clean package -U -DskipTests -P prod -e
```c
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] Building piflow-web 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- maven-resources-plugin:3.0.2:resources (default-resources) @ piflow-web ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 1 resource
[INFO] Copying 690 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.7.0:compile (default-compile) @ piflow-web ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- maven-resources-plugin:3.0.2:testResources (default-testResources) @ piflow-web ---
[INFO] Not copying test resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.7.0:testCompile (default-testCompile) @ piflow-web ---
[INFO] Not compiling test sources
[INFO] 
[INFO] --- maven-surefire-plugin:2.21.0:test (default-test) @ piflow-web ---
[INFO] Tests are skipped.
[INFO] 
[INFO] --- maven-jar-plugin:3.0.2:jar (default-jar) @ piflow-web ---
[INFO] Building jar: /home/nature/git_repository/piflow-web/piflow-web/target/piflow-web.war
[INFO] 
[INFO] --- spring-boot-maven-plugin:2.0.4.RELEASE:repackage (default) @ piflow-web ---
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 6.363 s
[INFO] Finished at: 2019-03-26T10:34:00+08:00
[INFO] Final Memory: 26M/97M
[INFO] ------------------------------------------------------------------------
```
To Run Piflow Web：
- Note: 
  - 1.The piflow server must be running before running piflow-web
  - 2.This version can choose any of "H2DB" and "MySQL" database
- run piflow web on intellij:

  - edit config.properties
  - build piflow to generate piflow-web.jar

- run piflow web by release version:

  - download piflow_release: https://github.com/cas-bigdatalab/piflow-web/releases/tag/v1.3
  - edit config.properties
  - sh start.sh
- how to configure config.properties
```c

server.port=6002
server.servlet.session.timeout=3600

syspara.interfaceUrlHead=http://127.0.0.1:8002
syspara.livyServer=http://127.0.0.1:8998
syspara.isIframe=true

# Total maximum value of uploaded files
spring.servlet.multipart.max-request-size=512MB
# Maximum value of a single file
spring.servlet.multipart.max-file-size=512MB

#If you want to use the H2DB database, please comment MySQL here
# data source
sysParam.datasource.type=mysql
# MySQL Configuration
#Configure the connection address of MySQL
spring.datasource.url = jdbc:mysql://10.0.85.81:3306/piflow_web_1.1?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true&autoReconnect=true&failOverReadOnly=false
#Configure database user name
spring.datasource.username=root
#Configuration database password
spring.datasource.password=123456
#Configure JDBC Driver
# Can not be configured, according to the URL automatic identification, recommended configuration
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

spring.flyway.locations=classpath:db/flyway-mysql/

#If you want to use H2DB database, please open the note here
## data source
#sysParam.datasource.type=h2
## h2 Configuration
##Configure the connection address of H2DB
#spring.datasource.url=jdbc:h2:file:/media/nature/linux_disk_0/PiFlow_DB/piflow_web
##Configure database user name
#spring.datasource.username=Admin
##Configuration database password
#spring.datasource.password=Admin
##Configure JDBC Driver
## Can not be configured, according to the URL automatic identification, recommended configuration
#spring.datasource.driver-class-name=org.h2.Driver
###H2DB web console settings
#spring.datasource.platform=h2
##After this configuration, h2 web consloe can be accessed remotely. Otherwise it can only be accessed locally.
#spring.h2.console.settings.web-allow-others=true
##With this configuration, you can access h2 web consloe through YOUR_URL / h2. YOUR_URL is the access URL of your program.
#spring.h2.console.path=/h2
##With this configuration, h2 web consloe will start when the program starts. Of course this is the default. If you don't want to start h2 web consloe when you start the program, then set it to false.
#spring.h2.console.enabled=true
#
#spring.flyway.locations=classpath:db/flyway-h2db/


# Log Coordination Standard
logging.level.org.flywaydb=warn
logging.level.com.nature.mapper=warn
logging.level.root=warn
logging.level.org.springframework.security=warn
logging.level.org.hibernate.SQL=warn
```




