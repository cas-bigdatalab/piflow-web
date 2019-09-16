![](https://github.com/cas-bigdatalab/piflow/blob/master/doc/piflow-logo2.png) 

[![GitHub releases](https://img.shields.io/github/release/cas-bigdatalab/piflow-web.svg)](https://github.com/cas-bigdatalab/piflow-web/releases)
[![GitHub downloads](https://img.shields.io/github/downloads/cas-bigdatalab/piflow-web/total.svg)](https://github.com/cas-bigdatalab/piflow-web/releases)
[![GitHub issues](https://img.shields.io/github/issues/cas-bigdatalab/piflow-web.svg)](https://github.com/cas-bigdatalab/piflow-web/issues)
[![GitHub forks](https://img.shields.io/github/forks/cas-bigdatalab/piflow-web.svg)](https://github.com/cas-bigdatalab/piflow-web/network)
[![GitHub stars](https://img.shields.io/github/stars/cas-bigdatalab/piflow-web.svg)](https://github.com/cas-bigdatalab/piflow-web/stargazers)
[![GitHub license](https://img.shields.io/github/license/cas-bigdatalab/piflow-web.svg)](https://github.com/cas-bigdatalab/piflow-web/blob/master/LICENSE)
## Requirements
* JDK 1.8 or newer
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
server.servlet.context-path=/piflow-web
server.servlet.session.timeout=600

syspara.videoName=exampleVideo.mp4
syspara.imagesPath=/data/piflowWeb/storage/image/
syspara.videosPath=/data/piflowWeb/storage/video/
syspara.xmlPath=/data/piflowWeb/storage/xml/
syspara.interfaceUrlHead=http://10.0.88.108:8002
syspara.isLoadStop=true
syspara.syncProcessCron=0/5 * * * * ?


spring.flyway.baselineOnMigrate=true
# Location of SQL file
spring.flyway.locations=classpath:db/

# data source
# Basic attributes
spring.datasource.name=dev
spring.datasource.url=jdbc:mysql://10.0.88.109:3306/piflow_web?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true&autoReconnect=true&failOverReadOnly=false
spring.datasource.username=root
spring.datasource.password=123456
# Can not be configured, according to the URL automatic identification, recommended configuration
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
###################The following configuration is added for Druid###########################
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
# Number of initialization connection pools
spring.datasource.initialSize=5
# Minimum number of connection pools (no longer in use, configuration is ineffective)
spring.datasource.minIdle=2
# Maximum number of connection pools
spring.datasource.maxActive=20
# Configuration takes time to get the connection to wait for a timeout in milliseconds. Fair locks are enabled by default, and concurrency efficiency decreases.
spring.datasource.maxWait=60000
# How often is the configuration interval detected to detect idle connections that need to be closed in milliseconds?
spring.datasource.timeBetweenEvictionRunsMillis=60000
# Configure the minimum lifetime of a connection in the pool in milliseconds
spring.datasource.minEvictableIdleTimeMillis=300000
# The SQL used to check whether the connection is valid requires a query statement.
# If validationQuery is null, testOnBorrow, testOnReturn, testWhileIdle will not work.
spring.datasource.validationQuery=SELECT 1 FROM DUAL
# It is recommended to configure true to not affect performance and ensure security.
# When the connection is requested, if the idle time is greater than timeBetweenEvictionRunsMillis, execute validationQuery to check if the connection is valid.
spring.datasource.testWhileIdle=true
# When you apply for a connection, execute validationQuery to check if the connection is valid. Doing this configuration will reduce performance.
spring.datasource.testOnBorrow=false
# When the connection is returned, the validationQuery is executed to check if the connection is valid. Doing this configuration will reduce the performance.
spring.datasource.testOnReturn=false
# Open the PSCache and specify the size of the PSCache on each connection
spring.datasource.poolPreparedStatements=true
spring.datasource.maxPoolPreparedStatementPerConnectionSize=20
# Configure extensions by alias, separated by multiple commas. Common plugins are:
# for monitoring statistics filter:stat
# for log filter:log4j
# Defense sql injection filter:wall
#spring.datasource.filters=stat,wall,log4j
spring.datasource.filters=stat,log4j
# Open the mergeSql function via the connectProperties property; slow SQL logging
spring.datasource.connectionProperties=druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
# Merge monitoring data from multiple Druid Data Sources



# template engine thymeleaf
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.mode=HTML5
spring.thymeleaf.encoding=UTF-8

#Hot deployment files, pages do not generate caches, timely updates
spring.thymeleaf.cache=false
spring.resources.chain.strategy.content.enabled=true
spring.resources.chain.strategy.content.paths=/**


# Configuring static resources
spring.mvc.view.prefix=/templates/
spring.mvc.view.suffix=.html
spring.mvc.static-path-pattern=/**


# Configuration automatic table building: updata: no table new, table update operation, console display table building statement
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=true

#Hibernate defaults to create a MySQL table in Springboot that is MyISAM engine and is specified as InnoDB because it does not support things.
spring.jpa.database-platform=org.hibernate.dialect.MySQL5InnoDBDialect


# integration mybatis
mybatis.type-aliases-package=com.nature.**.model
#Open Hump Mapping
mybatis.configuration.map-underscore-to-camel-case=true
#Turn on lazy loading
#Global settings are lazy to load. If set to `false', all associations are initially loaded.
mybatis.configuration.lazy-loading-enabled=true
#When set to'true', lazy objects may be loaded by all of the lazy attributes. Otherwise, each attribute is loaded on demand.
mybatis.configuration.aggressive-lazy-loading=false

# Log Coordination Standard
logging.level.com.nature.mapper=warn
logging.level.root=warn
logging.level.org.springframework.security=warn

# Log Coordination. XML
#logging.config.classpath=log4j2.xml
```




