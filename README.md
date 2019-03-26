![](https://github.com/cas-bigdatalab/piflow/blob/master/doc/piflow-logo2.png) 
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

- run piflow server on intellij:

+ execute MySQL database building table script
+ edit config.properties
+ build piflow to generate piflow-web.jar

- run piflow web by release version:

  - download piflow_release: 
  - copy the piflow-web.jar to the piflow_release folder
  - copy the create database.sql to the piflow_release folder
  - copy the create table.sql to the piflow_release folder
  - copy the create init data.sql to the piflow_release folder
  - edit config.properties
  - run app.sh start
- how to configure config.properties
```c
server.port=6001
server.servlet.context-path=/piflow-web
server.servlet.session.timeout=600


syspara.videoName=exampleVideo.mp4
syspara.imagesPath=/opt/piflowWeb/storage/image/
syspara.videosPath=/opt/piflowWeb/storage/video/
syspara.xmlPath=/opt/piflowWeb/storage/xml/
syspara.interfaceUrlHead=http://10.0.88.109:8002
syspara.isLoadStop=true
syspara.syncProcessCron=0/5 * * * * ?

# data source
spring.datasource.url=jdbc:mysql://192.168.254.196:3306/piflow_web?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true&autoReconnect=true&failOverReadOnly=false
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
#type: com.alibaba.druid.pool.DruidDataSource


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

#Springboot中Hibernate默认创建的mysql表为myisam引擎,因不支持事物所以指定为：innodb
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




