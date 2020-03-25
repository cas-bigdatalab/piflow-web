![](https://github.com/cas-bigdatalab/piflow/blob/master/doc/piflow-logo2.png) 

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
To Build: mvn package -Dmaven.test.skip=true
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
[INFO] Building jar: /home/nature/git_repository/piflow-web/piflow-web/target/piflow-web.jar
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
  - 2.Dynamically configured storage directories must be created manually(syspara.imagesPath,syspara.videosPath,syspara.xmlPath)
- run piflow server on intellij:

  - edit config.properties
  - build piflow to generate piflow-web.jar

- run piflow web by release version:

  - download piflow_release: https://github.com/cas-bigdatalab/piflow-web/releases/tag/0.5
  - edit config.properties
  - run sh start.sh
- how to configure config.properties
```c
server.port=6001
server.servlet.session.timeout=3600

syspara.interfaceUrlHead=http://10.0.88.108:8002
syspara.isIframe=true


# data source
sysParam.datasource.type=mysql
# MySQL Configuration
#Configure the connection address of MySQL
spring.datasource.url = jdbc:mysql://10.0.88.109:3306/piflow_web?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true&autoReconnect=true&failOverReadOnly=false
#Configure database user name
spring.datasource.username=root
#Configuration database password
spring.datasource.password=123456
#Configure JDBC Driver
# Can not be configured, according to the URL automatic identification, recommended configuration
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

spring.flyway.locations=classpath:db/flyway-mysql/



# Log Coordination Standard
logging.level.org.flywaydb=debug
logging.level.com.nature.mapper=debug
logging.level.root=info
logging.level.org.springframework.security=info
logging.level.org.hibernate.SQL=DEBUG


```




