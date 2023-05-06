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
[INFO] --- maven-resources-plugin:3.2.0:testResources (default-testResources) @ piflow-web ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Using 'UTF-8' encoding to copy filtered properties files.
[INFO] skip non existing resourceDirectory /zData/workspaces/PiFlow/piflow-web/piflow-web/src/test/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.8.1:testCompile (default-testCompile) @ piflow-web ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 33 source files to /zData/workspaces/PiFlow/piflow-web/piflow-web/target/test-classes
[INFO] 
[INFO] --- maven-surefire-plugin:2.22.2:test (default-test) @ piflow-web ---
[INFO] Tests are skipped.
[INFO] 
[INFO] --- maven-war-plugin:3.3.1:war (default-war) @ piflow-web ---
[INFO] Packaging webapp
[INFO] Assembling webapp [piflow-web] in [/zData/workspaces/PiFlow/piflow-web/piflow-web/target/piflow-web]
[INFO] Processing war project
[INFO] Building war: /workspaces/PiFlow/piflow-web/piflow-web/target/piflow-web.war
[INFO] 
[INFO] --- spring-boot-maven-plugin:2.4.0:repackage (repackage) @ piflow-web ---
[INFO] Replacing main artifact with repackaged archive
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  30.800 s
[INFO] Finished at: 2022-11-03T12:44:58+08:00
[INFO] ------------------------------------------------------------------------
[WARNING] The requested profile "downloadSources" could not be activated because it does not exist.
```
To Run Piflow Web：
- Note: 
  - 1.The piflow server must be running before running piflow-web
  - 2.This version can only choose "MySQL" database
- run piflow web on intellij:

  - edit config.properties
  - build piflow to generate piflow-web.jar

- run piflow web by release version:

  - download piflow_release: https://github.com/cas-bigdatalab/piflow-web/releases/tag/v1.5
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

# Log Coordination Standard
logging.level.root=info
logging.level.org.flywaydb=info
logging.level.org.springframework.security=info
#logging.level.cn.cnic.component.testData.mapper.*=debug

# if you need to upload python stop,please set docker.host
#docker.host=tcp://localhost:2375
#if you want to push docker images,please set these params
#docker.tls.verify=false
#docker.registry.url=http://localhost:7185
#docker.project.name=piflow
#docker.registry.user.name=admin
#docker.registry.password=admin
```




