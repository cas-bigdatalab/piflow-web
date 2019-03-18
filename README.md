![](https://github.com/cas-bigdatalab/piflow/blob/master/doc/piflow-logo2.png) 
## Requirements
* JDK 1.8 or newer
* Apache Maven 3.1.0 or newer
* Apache Tomcat 8.5 or newer
## Getting Started
- `how to configure application.yml`
server:
  port: 7001
  servlet:
    context-path: /piflow-web
    session:
      timeout: 600
syspara:
  imagesPath: /piflow/image/
  videosPath: /piflow/video/
  xmlPath: /piflow/xml/
  interfaceUrlHead : http://10.0.88.108:8002
spring:
#Configuring data sources
  datasource:
    url: jdbc:mysql://10.0.88.109:3306/piflow_web?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true
    username: root
    password: 123456
    driver-class-name: com.mysql.jdbc.Driver
    #type: com.alibaba.druid.pool.DruidDataSource
#thymeleaf
  thymeleaf:
    prefix: classpath:/templates/
    suffix: .html
    mode: HTML5
    encoding: UTF-8
#Configure Hot Deployment Files, Pages do not generate caches, more timely
    cache:  false
  resources:
    chain:
      strategy:
        content:
          enabled: true
          paths: /**
#Configuring static resources
  mvc:
    view:
      prefix: /templates/
      suffix: .html
    static-path-pattern: /**
#Configuration automatic table building: updata: no table new, table update operation, console display table building statement
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    open-in-view: true
#Hibernate defaults to create a MySQL table in Springboot that is MyISAM engine and is specified as InnoDB because it does not support things.
    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect

#Integrating mybatis
mybatis:
  type-aliases-package: com.nature.**.model

  configuration:
    #Open Hump Mapping
    map-underscore-to-camel-case: true
    #Turn on lazy loading
    #Global settings are lazy to load. If set to `false', all associations are initially loaded.
    lazyLoadingEnabled: true
    #When set to'true', lazy objects may be loaded by all of the lazy attributes. Otherwise, each attribute is loaded on demand.
    aggressive-lazy-loading: false

#Log matching
logging:
  level:
    com.nature.mapper : debug
    root: info
    org:
      springframework:
        security: info

